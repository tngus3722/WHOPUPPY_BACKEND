package com.whopuppy.service;

import com.whopuppy.domain.user.AuthNumber;
import com.whopuppy.domain.user.User;
import com.whopuppy.response.BaseResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface UserService {
    Map<String,String> login(User user)throws Exception;
    Map<String,Object> refresh()throws Exception;
    void signUp(User user) throws Exception;
    String sendSms(AuthNumber authNumber) throws Exception;
    String configSms(AuthNumber authNumber) throws Exception;
    String nicknameCheck(String nickname);
    void passwordUpdate(User user)throws  Exception;
    User getMe();
    String accountCheck(String account);
    String setProfile(MultipartFile multipartFile)throws Exception;
    Long getLoginUserId();
    User getMe(String token);
    BaseResponse updateNickname(String nickname);
}