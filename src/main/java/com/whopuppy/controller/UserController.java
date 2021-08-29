package com.whopuppy.controller;


import com.whopuppy.annotation.Auth;
import com.whopuppy.annotation.ValidationGroups;
import com.whopuppy.annotation.Xss;
import com.whopuppy.domain.user.AuthNumber;
import com.whopuppy.domain.user.User;
import com.whopuppy.response.BaseResponse;
import com.whopuppy.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Resource
    @Qualifier("UserServiceImpl")
    private UserService userService;


    // 인증번호 발송 
    @Xss
    @PostMapping(value = "/sms")
    public ResponseEntity sendSms(@Validated(ValidationGroups.sendSms.class) @RequestBody AuthNumber authNumber) throws Exception {
        return new ResponseEntity(new BaseResponse(userService.sendSms(authNumber), HttpStatus.OK), HttpStatus.OK);
    }

    // 인증번호 인증
    @Xss
    @PostMapping(value = "/sms/config")
    public ResponseEntity configSms(@Validated(ValidationGroups.configSms.class) @RequestBody AuthNumber authNumber) throws Exception {
        return new ResponseEntity(new BaseResponse(userService.configSms(authNumber), HttpStatus.OK), HttpStatus.OK);
    }

    // 회원 가입 
    @Xss
    @PostMapping(value = "/sign-up")
    public ResponseEntity signUp(@RequestBody @Validated(ValidationGroups.signUp.class) User user) throws Exception {
        userService.signUp(user);
        return new ResponseEntity(new BaseResponse("회원가입에 성공했습니다.", HttpStatus.OK), HttpStatus.OK);
    }

    // 로그인
    @Xss
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ApiOperation(value = "로그인", notes = "로그인을 하기위한 api입니다.")
    public ResponseEntity login(@RequestBody @Validated(ValidationGroups.logIn.class) User user) throws Exception {
        return new ResponseEntity(userService.login(user), HttpStatus.OK);
    }

    // token 재발급
    @ApiOperation(value = "access 토큰 갱신", notes = "refresh token을 이용하여 access token을 갱신하는 api 입니다.", authorizations = @Authorization(value = "Bearer +refreshToken"))
    @RequestMapping(value = "/refresh", method = RequestMethod.POST)
    public ResponseEntity refresh() throws Exception {
        return new ResponseEntity(userService.refresh(), HttpStatus.OK);
    }

    // 비밀번호 재발급
    @Xss
    @RequestMapping(value = "/password", method = RequestMethod.PUT)
    @ApiOperation(value = "비밀번호 재설정", notes = "비밀번호 재 설정을 위한 api입니다. 이메일 인증이 선행되어야 합니다.")
    public ResponseEntity passwordUpdate(@RequestBody @Validated(ValidationGroups.findPassword.class) User user) throws Exception {
        userService.passwordUpdate(user);
        return new ResponseEntity(new BaseResponse("비밀번호 재설정에 성공했습니다.", HttpStatus.OK), HttpStatus.OK);
    }

    // 닉네임 중복 체크
    @Xss
    @ApiOperation(value = "닉네임 중복체크", notes = "닉네임 중복 체크 api입니다")
    @RequestMapping(value = "/nickname", method = RequestMethod.POST)
    public ResponseEntity nicknameCheck(@RequestBody @Validated(ValidationGroups.nicknameCheck.class) User user) {
        return new ResponseEntity(new BaseResponse(userService.nicknameCheck(user.getNickname()), HttpStatus.OK), HttpStatus.OK);
    }

    //계정 중복 체크
    @Xss
    @ApiOperation(value = "계정 중복체크", notes = "계정 중복 체크 api입니다")
    @RequestMapping(value = "/account", method = RequestMethod.POST)
    public ResponseEntity accountCheck(@RequestBody @Validated(ValidationGroups.accountCheck.class) User user) {
        return new ResponseEntity(new BaseResponse(userService.accountCheck(user.getAccount()), HttpStatus.OK), HttpStatus.OK);
    }

    //유저의 정보
    @Auth
    @ApiOperation(value = "로그인한 유저의 정보", notes = "현재 로그인한 유저의 정보를 return합니다. 로그인을 하지 않은 상태일 때는 null을 return합니다.", authorizations = @Authorization(value = "Bearer +accessToken"))
    @GetMapping(value = "/me")
    public ResponseEntity getMe() {
        return new ResponseEntity(userService.getMe(), HttpStatus.OK);
    }

    //
    @Auth
    @ApiOperation(value = "프로필 사진 설정  ", notes = "프로필 사진 설정", authorizations = @Authorization(value = "Bearer +accessToken"))
    @PostMapping(value = "/profile")
    public ResponseEntity setProfile(@RequestBody MultipartFile multipartFile) throws Exception {
        return new ResponseEntity(new BaseResponse(userService.setProfile(multipartFile), HttpStatus.OK), HttpStatus.OK);
    }


    @Xss
    @Auth
    @RequestMapping(value = "/nickname", method = RequestMethod.PUT)
    @ApiOperation(value = "닉네임 변경", notes = "닉네임 변경", authorizations = @Authorization(value = "Bearer +accessToken"))
    public ResponseEntity updateNickname(@RequestBody String nickname) throws Exception {
        return new ResponseEntity(userService.updateNickname(nickname), HttpStatus.OK);
    }
    
}
