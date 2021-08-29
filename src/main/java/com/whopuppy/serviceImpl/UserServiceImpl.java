package com.whopuppy.serviceImpl;

import com.whopuppy.domain.user.AuthNumber;
import com.whopuppy.domain.user.User;
import com.whopuppy.enums.ErrorMessage;
import com.whopuppy.exception.AccessTokenInvalidException;
import com.whopuppy.exception.CriticalException;
import com.whopuppy.exception.RefreshTokenInvalidException;
import com.whopuppy.exception.RequestInputException;
import com.whopuppy.mapper.UserMapper;
import com.whopuppy.response.BaseResponse;
import com.whopuppy.service.UserService;
import com.whopuppy.util.CoolSmsUtil;
import com.whopuppy.util.JwtUtil;
import com.whopuppy.util.S3Util;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.*;

@Transactional
@Service("UserServiceImpl")
public class UserServiceImpl implements UserService {

    @Autowired
    private CoolSmsUtil coolSmsUtil;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private S3Util s3Util;


    @Value("${token.access}")
    private String access_token;

    @Value("${token.refresh}")
    private String refresh_token;
    @Value("${refresh.user.name}")
    private String refreshUserName;
    @Value("${token.user.name}")
    private String accessTokenName;

    @Override
    public Map<String, String> login(User user) throws Exception {

        //계정 이름으로 user를 가저옴
        User dbUser = userMapper.getUserIdFromAccount(user.getAccount());

        // 없는 아이디라면
        if (dbUser == null) {
            throw new RequestInputException(ErrorMessage.ACCOUNT_FAIL);
        }
        // 비밀번호가 틀린 경우
        if (!BCrypt.checkpw(user.getPassword(), dbUser.getPassword())) {
            throw new RequestInputException(ErrorMessage.PASSWORD_FAIL);
        }
        // 로그인이 성공한 경우 , access token, refresh token 반환
        else {
            Map<String, String> token = new HashMap<>();
            token.put(access_token, jwtUtil.generateToken(dbUser.getId(), dbUser.getNickname(), access_token));
            token.put(refresh_token, jwtUtil.generateToken(dbUser.getId(), dbUser.getNickname(), refresh_token));
            return token;
        }
    }

    @Override
    public Map<String, Object> refresh() throws Exception {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String refreshToken = request.getHeader(refreshUserName);
        int result = jwtUtil.isValid(refreshToken, 1);
        if (result == 1) { // valid하다면
            Map<String, Object> payloads = jwtUtil.validateFormat(refreshToken, 1);
            Long id = Long.valueOf(String.valueOf(payloads.get("id")));
            String nickname = String.valueOf(payloads.get("nickname"));
            Map<String, Object> token = new HashMap<>();
            token.put(access_token, jwtUtil.generateToken(id, nickname, access_token));
            token.put(refresh_token, jwtUtil.generateToken(id, nickname, refresh_token));
            return token;
        } else if (result == 0) {
            throw new RefreshTokenInvalidException(ErrorMessage.REFRESH_FORBIDDEN_AUTH_INVALID_EXCEPTION); // REFRESH 토근에 ACCESS 토근이 들어온 경우
        } else {
            throw new RefreshTokenInvalidException(ErrorMessage.UNDEFINED_EXCEPTION); // pass도 expire도 invalid도 아닌경우 발생
        }
    }

    @Override
    public void signUp(User user) throws Exception {


        // 닉네임 null,중복 체크
        if (user.getNickname() != null) {
            if (userMapper.getUserByNickName(user.getNickname()) != null) {
                throw new RequestInputException(ErrorMessage.NICKNAME_DUPLICATE);
            }
        }

        // account 를 통한 중복가입 여부 확인
        if (userMapper.getUserIdFromAccount(user.getAccount()) != null) {
            throw new RequestInputException(ErrorMessage.ACCOUNT_ALREADY_SIGNED_UP);
        }

        // 번호인증을 했는가?
        AuthNumber dbValue = userMapper.getAuthTrue(user.getAccount(), 0, user.getPhone_number());
        if (dbValue == null) {
            throw new RequestInputException(ErrorMessage.SMS_NONE_AUTH_EXCEPTION);
        }

        // 번호인증의 secret값이 맞는가 ?
        if (!dbValue.getSecret().equals(user.getSecret())) {
            throw new RequestInputException(ErrorMessage.SMS_SECRET_INVALID_EXCEPTION);
        }

        // 비밀번호 암호화
        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        userMapper.signUp(user); // 회원가입


        //회원가입후 user의 가입된 id를 구함
        User dbUser = userMapper.getUserIdFromAccount(user.getAccount());
        // 재전송의 경우 phoneNumber, flag, ip가 같은 이전 이력은 모두 만료 + soft delete 시킴

        // 회원가입 완료 시  phoneNumber, flag, ip가 같은 이전 이력은 모두 만료 + soft delete 시킴
        AuthNumber authNumber = new AuthNumber();
        authNumber.setIp(this.getClientIp());
        authNumber.setPhone_number(user.getPhone_number());
        authNumber.setFlag(0); // 회원가입 인증
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_YEAR, -1); // 현재시간 빼기 하루
        authNumber.setExpired_at(new Timestamp(calendar.getTimeInMillis()));
        userMapper.expirePastAuthNumber(authNumber);

        // user salt = timestamp + user_id + BCrypt
        // salt 삽입
        calendar.setTime(new Date());
        if (dbUser != null) {
            String salt = dbUser.getId().toString() + calendar.getTime();
            salt = (BCrypt.hashpw(salt, BCrypt.gensalt()));
            userMapper.setSalt(salt, dbUser.getId());
        } else { // 발생 불가능
            throw new CriticalException(ErrorMessage.UNDEFINED_EXCEPTION);
        }

    }

    // 문자 발송
    @Override
    public String sendSms(AuthNumber authNumber) throws Exception {
        // 와이파이가 중간에 끊겼을 경우 ip 체크는 문제가 될수있다
        // 게정의 회원이 있는지 없는지는 폰번호로 하는 것이 올바륻 ㅏ.
        System.out.println(this.getClientIp());
        if (authNumber.getFlag() == 0) { // 회원가입 발송
            Long id = userMapper.getUserIdFromPhoneNumber(authNumber.getPhone_number()); // 폰번호 검색
            if (id != null) { // 가입한 아이디라면
                throw new RequestInputException(ErrorMessage.SMS_ALREADY_AUTHED); // 이미 가입했다는 에러 발생
            }
        }

        //비밀번호 찾기 요청이라면, 가입하지 않은 아이디면 throw
        if (authNumber.getFlag() == 1) {
            Long id = userMapper.getUserIdFromPhoneNumber(authNumber.getPhone_number());
            if (id == null) {
                throw new RequestInputException(ErrorMessage.NO_USER_EXCEPTION);
            }
        }

        // 1일 5회 요청제한을 넘겼는지
        String ip = this.getClientIp();
        Calendar calendar = Calendar.getInstance(); // 싱글톤 객체라긔
        calendar.setTime(new Date());
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.set(year, month, day, 0, 0, 0); // 해당 날짜의 00시 00분 00초
        Timestamp start = new Timestamp(calendar.getTimeInMillis());
        calendar.set(year, month, day, 23, 59, 59); // 해당 날짜의 23시 59분 59초
        Timestamp end = new Timestamp(calendar.getTimeInMillis());
        Integer count = userMapper.authNumberAllSoftDeleteAfterUse(authNumber.getPhone_number(), ip, start, end);
        if (count >= 5) {
            throw new RequestInputException(ErrorMessage.SMS_DAY_REQUEST_COUNT_EXCCED); // 요청한 날의 요청횟수가 5번을 초과한경우
        }

        // 재전송의 경우 phoneNumber, flag, ip가 같은 이전 이력은 모두 만료 + soft delete 시킴
        authNumber.setIp(ip);
        calendar.setTime(new Date()); // 다시 현재시간 
        calendar.add(Calendar.DAY_OF_YEAR, -1); // 현재시간 빼기 하루
        authNumber.setExpired_at(new Timestamp(calendar.getTimeInMillis()));
        userMapper.expirePastAuthNumber(authNumber);

        // 6자리의 랜덤숫자열 생성
        String secret = "";
        Random rnd = new Random();
        for (int i = 0; i < 6; i++) {
            secret += rnd.nextInt(10);// 글자의 random numbers
        }

        //db에 전송 이력을 저장
        calendar = Calendar.getInstance();
        authNumber.setSecret(secret);
        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE, 10); // 만료기한 10분
        authNumber.setExpired_at(new Timestamp((calendar.getTime()).getTime()));

        secret = "<#> 인증번호 [" + secret + "]\n" + authNumber.getCode();
        userMapper.setAuthNumber(authNumber);
        coolSmsUtil.singleSms(authNumber.getPhone_number(), secret);// sms 발송

        return "sms를 발송 했습니다.";
    }

    @Override
    public String configSms(AuthNumber authNumber) throws Exception {

        // 와이파이가 중간에 끊겼을 경우 ip 체크는 문제가 될수있다
        //회원가입 요청이라면
        if (authNumber.getFlag() == 0) {
            // 이미 가입된 번호인지 체크
            Long id = userMapper.getUserIdFromPhoneNumber(authNumber.getPhone_number());
            if (id != null) {
                throw new RequestInputException(ErrorMessage.SMS_ALREADY_AUTHED);
            }
        }
        //비밀번호 찾기 요청이라면,
        if (authNumber.getFlag() == 1) {
            //가입한 아이디가 없다면
            Long id = userMapper.getUserIdFromPhoneNumber(authNumber.getPhone_number());
            if (id == null) {
                throw new RequestInputException(ErrorMessage.NO_USER_EXCEPTION);
            }
        }

        //account, flag, phoneNumber 값으로 select 해옴
        // 폰넘버 flag -- select하는게 맞는것같다
        AuthNumber dbValue = userMapper.getSecret(authNumber);

        //문자를 보낸적 없다면 email인증을 신청하라고 알림
        if (dbValue == null) {
            throw new RequestInputException(ErrorMessage.SMS_NONE_AUTH_EXCEPTION);
        }

        //request secret값이 일치하는지 확인
        AuthNumber dbAuthNumber = null;
        if (dbValue.getSecret().equals(authNumber.getSecret())) {
            dbAuthNumber = dbValue;
        }

        // secret 값이 다르다면 인증번호를 확인하라는 알림
        if (dbAuthNumber == null) {
            throw new RequestInputException(ErrorMessage.SMS_SECRET_INVALID_EXCEPTION);
        }

        //만료시간보다 현재시간이 크다면 만료되었다고 알림
        Timestamp exp = dbAuthNumber.getExpired_at();
        Timestamp now = new Timestamp(System.currentTimeMillis());
        if (exp.getTime() < now.getTime()) {
            userMapper.authNumberSoftDelete(dbAuthNumber.getId()); // 만료시 soft delete
            throw new RequestInputException(ErrorMessage.SMS_EXPIRED_AUTH_EXCEPTION);
        }
        // 만료되지않았고 / secret 같고 // account 같고 // ip 같다면 ==> is_authed = 1
        userMapper.setIs_authed(true, dbAuthNumber.getId());

        return "sms 인증에 성공했습니다.";
    }

    @Override
    public String nicknameCheck(String nickname) {
        String result = userMapper.getUserByNickName(nickname);
        if (result != null)  // 닉네임이 이미 존재 한다면
            throw new RequestInputException(ErrorMessage.NICKNAME_DUPLICATE);
        else
            return "사용 가능한 닉네임 입니다";

    }

    @Override
    public BaseResponse updateNickname(String nickname) {
        String result = userMapper.getUserByNickName(nickname);
        if (result != null)  // 닉네임이 이미 존재 한다면
            throw new RequestInputException(ErrorMessage.NICKNAME_DUPLICATE);
        else
            userMapper.UpdateNickname(nickname, this.getLoginUserId());
        return new BaseResponse("닉네임이 변경되었습니다.", HttpStatus.OK);
    }

    //유저의 계정과 비밀번호를 입력받아, 문자 인증이 진행 되었는지 확인 후 변경한다.
    @Override
    public void passwordUpdate(User user) throws Exception {
        User dbUser = userMapper.getUserIdFromAccount(user.getAccount());
        // 없는 아이디는 아닌지?
        if (dbUser == null) {
            throw new RequestInputException(ErrorMessage.NO_USER_EXCEPTION);
        }

        //번호 인증 여부 체크
        AuthNumber dbValue = userMapper.getAuthTrue(user.getAccount(), 1, user.getPhone_number());
        if (dbValue == null) {
            throw new RequestInputException(ErrorMessage.SMS_NONE_AUTH_EXCEPTION);
        }
        // 인증번호가 틀렸는지 확인
        if (!dbValue.getSecret().equals(user.getSecret())) {
            throw new RequestInputException(ErrorMessage.SMS_SECRET_INVALID_EXCEPTION);
        }

        // 비밀번호 암호화
        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));


        // 비밀번호 찾기 완료 시  phoneNumber, flag, ip가 같은 이전 이력은 모두 만료 + soft delete 시킴
        AuthNumber authNumber = new AuthNumber();
        authNumber.setIp(this.getClientIp());
        authNumber.setPhone_number(user.getPhone_number());
        authNumber.setFlag(1); // 비밀번호 찾기 인증
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_YEAR, -1); // 현재시간 빼기 하루
        authNumber.setExpired_at(new Timestamp(calendar.getTimeInMillis()));
        userMapper.expirePastAuthNumber(authNumber);
        userMapper.passwordUpdate(user);
    }

    @Override
    public String accountCheck(String account) {
        User user = userMapper.getUserIdFromAccount(account);
        if (user != null) {
            throw new RequestInputException(ErrorMessage.ACCOUNT_ALREADY_SIGNED_UP);
        } else {
            return "사용 가능한 계정명 입니다.";
        }
    }

    // get client ip
    private String getClientIp() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null) {
            ip = request.getHeader("WL-Proxy-Client-IP"); // 웹로직
        }
        if (ip == null) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    @Override
    public User getMe() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader(accessTokenName);
        return getMe(token);
    }

    @Override
    public User getMe(String token) {
        if (token == null)
            return null;

        else {
            if (jwtUtil.isValid(token, 0) == 0) {
                Map<String, Object> payloads = jwtUtil.validateFormat(token, 0);
                Long id = Long.valueOf(String.valueOf(payloads.get("id")));
                return userMapper.getMe(id);
            } else
                throw new AccessTokenInvalidException(ErrorMessage.ACCESS_FORBIDDEN_AUTH_INVALID_EXCEPTION);
        }
    }

    @Override
    public String setProfile(MultipartFile multipartFile) throws Exception {
        // multipartfile이 null인 경우
        if (multipartFile == null) {
            throw new RequestInputException(ErrorMessage.MULTIPART_FILE_NULL);
        }
        //multipartfile의 content type이 jpeg, png가 아닌경우

        if (!multipartFile.getContentType().equals("image/jpeg") && !multipartFile.getContentType().equals("image/png")) {
            throw new RequestInputException(ErrorMessage.MULTIPART_FILE_NOT_IMAGE);
        }
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader(accessTokenName);
        if (token == null) {
            return "로그인을 해주세요";
        } else {
            // user id로 User를 select 하는것은 자유롭게 해도 좋으나, salt값은 조회,수정 하면안된다. 만약 참고할 일이있으면 정수현에게 다렉을 보내도록하자.
            if (jwtUtil.isValid(token, 0) == 0) {
                Map<String, Object> payloads = jwtUtil.validateFormat(token, 0);
                Long id = Long.valueOf(String.valueOf(payloads.get("id")));
                String url = s3Util.uploadObject(multipartFile);
                userMapper.setProfile(id, url);
            } else {
                throw new AccessTokenInvalidException(ErrorMessage.ACCESS_FORBIDDEN_AUTH_INVALID_EXCEPTION);
            }
        }
        return "프로필 사진이 설정되었습니다";
    }

    @Override
    public Long getLoginUserId() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader(accessTokenName);
        if (token == null) {
            throw new AccessTokenInvalidException(ErrorMessage.ACCESS_FORBIDDEN_AUTH_INVALID_EXCEPTION);
        } else {
            // user id로 User를 select 하는것은 자유롭게 해도 좋으나, salt값은 조회,수정 하면안된다. 만약 참고할 일이있으면 정수현에게 다렉을 보내도록하자.
            if (jwtUtil.isValid(token, 0) == 0) {
                Map<String, Object> payloads = jwtUtil.validateFormat(token, 0);
                Long id = Long.valueOf(String.valueOf(payloads.get("id")));
                return id;
            } else {
                throw new AccessTokenInvalidException(ErrorMessage.ACCESS_FORBIDDEN_AUTH_INVALID_EXCEPTION);
            }
        }
    }
}
