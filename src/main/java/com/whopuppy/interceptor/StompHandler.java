package com.whopuppy.interceptor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.whopuppy.domain.user.User;
import com.whopuppy.enums.ErrorMessage;
import com.whopuppy.exception.AccessTokenInvalidException;
import com.whopuppy.service.UserService;
import com.whopuppy.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

@Component
public class StompHandler implements ChannelInterceptor {
    @Autowired
    private JwtUtil jwt;

    @Autowired
    @Qualifier("UserServiceImpl")
    private UserService userService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor headerAccessor= StompHeaderAccessor.wrap(message);
        String accessToken = headerAccessor.getFirstNativeHeader("access_token");
        StompCommand command = headerAccessor.getCommand();
        System.out.println(command);
        System.out.println(message.toString());
        int result = jwt.isValid(accessToken, 0); // flag 0 -> access / 1 refresh
        if (result != 0)
            throw new AccessTokenInvalidException(ErrorMessage.ACCESS_FORBIDDEN_AUTH_INVALID_EXCEPTION);


        if (StompCommand.SUBSCRIBE.equals(command)) {
            System.out.println(headerAccessor.getFirstNativeHeader("destination"));

            User user = userService.getMe(accessToken);

            System.out.println(user.toString());
        }




         // access token이며 valid 하다면



//        if (StompCommand.SUBSCRIBE.equals(command)) {
//            //User me = userService.getMe(accessToken);
//
//            System.out.println("SUBSCRIBE " + message.toString());
//
//        }
//        }
//        try {
//            System.out.println(new ObjectMapper().writeValueAsString(message.getHeaders()));
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
//        if(StompCommand.SEND.equals(command)){
//            System.out.println("SEND " + message.toString());
//
//
//        }
//        if (StompCommand.SUBSCRIBE.equals(command)){
//            System.out.println("SUBSCRIBE " + message.toString());
//
//        }else if(StompCommand.CONNECT.equals(command)){
//            System.out.println("CONNECT " + message.toString());
//
//        }

        System.out.println("하이 " + accessToken);
        return message;
    }


}
