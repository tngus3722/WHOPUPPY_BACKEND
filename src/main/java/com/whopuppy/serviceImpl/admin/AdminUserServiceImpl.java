package com.whopuppy.serviceImpl.admin;

import com.whopuppy.annotation.Auth;
import com.whopuppy.domain.user.AuthNumber;
import com.whopuppy.domain.user.User;
import com.whopuppy.enums.ErrorMessage;
import com.whopuppy.exception.RequestInputException;
import com.whopuppy.mapper.UserMapper;
import com.whopuppy.service.admin.AdminUserService;
import com.whopuppy.serviceImpl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Service
public class AdminUserServiceImpl extends UserServiceImpl implements AdminUserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public String grantAuthority(AuthNumber authNumber){
        User user = userMapper.getUserIdFromAccount(authNumber.getAccount());

        //없는 유저에 경우 Error
        if ( user == null)
            throw new RequestInputException(ErrorMessage.NO_USER_EXCEPTION);

        String role = userMapper.getRole(user.getId());

        // 대상이 루트 유저인 경우 불가능
        if ( role != null && role.equals(Auth.Role.ROOT.toString()))
            throw new RequestInputException(ErrorMessage.ROOT_AUTHORITY_CAN_NOT_DELETE_EXCEPTION);

        //부여하고자 하는 권한
        String target = this.getTarget(authNumber.getFlag());

        // 유저의 권한 리스트를 찾아옴
        List<String> list = userMapper.getAuthorityById(user.getId());
        boolean check = false;
        for (int i=0; i<list.size(); i++){
            if ( list.get(i).equals(target)){
                check = true;
            }
        }
        // 이미 있는 권한이라면
        if (check){
            return "해당 유저가 이미 권한을 가지고 있습니다.";
        }

        //권한 부여
        userMapper.grantAuthority(user.getId(), authNumber.getFlag());
        return "권한을 부여했습니다.";
    }
    public String deleteAuthority(AuthNumber authNumber){
        User user = userMapper.getUserIdFromAccount(authNumber.getAccount());

        //없는 유저에 경우 Error
        if ( user == null)
            throw new RequestInputException(ErrorMessage.NO_USER_EXCEPTION);

        String role = userMapper.getRole(user.getId());
        
        // 대상이 일반 유저인 경우 불가능
        if ( role == null)
            throw new RequestInputException(ErrorMessage.USER_IS_NOT_MANAGER);
        // 대상이 루트 유저인 경우 불가능
        if ( role.equals(Auth.Role.ROOT.toString()))
            throw new RequestInputException(ErrorMessage.ROOT_AUTHORITY_CAN_NOT_DELETE_EXCEPTION);

        //부여하고자 하는 권한
        String target = this.getTarget(authNumber.getFlag());

        // 유저의 권한 리스트를 찾아옴
        List<String> list = userMapper.getAuthorityById(user.getId());
        boolean check = false;
        for (int i=0; i<list.size(); i++){
            if ( list.get(i).equals(target)){
                check = true;
            }
        }
        // 없는 권한이라면
        if (!check){
            return "제거하려는 권한을 가지고 있지 않은 유저입니다";
        }

        //권한 제거
        userMapper.deleteAuthority(user.getId(), authNumber.getFlag());
        return "권한을 제거하였습니다.";
    }
    private String getTarget(Integer flag){
        switch (flag){
            case 1:
                return Auth.Authority.WANT_DO_ADOPT.toString();
            case 2:
                return Auth.Authority.WANT_TAKE_ADOPT.toString();
            case 3:
                return Auth.Authority.ADOPT_REVIEW.toString();
            case 4:
                return Auth.Authority.SNACK.toString();
            default:
                // spring validation에서 체크가 되기 때문에 발생하지 않는 에러이다.
                throw new RequestInputException(ErrorMessage.AUTHORITY_NOT_EXIST);
        }
    }
}
