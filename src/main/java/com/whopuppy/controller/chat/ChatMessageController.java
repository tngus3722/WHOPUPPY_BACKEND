package com.whopuppy.controller.chat;

import com.whopuppy.domain.chat.ChatMessage;
import com.whopuppy.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

@Controller
public class ChatMessageController {
    @Autowired
    @Qualifier("stompChatServiceImpl")
    private ChatService chatService;

    @Autowired
    SimpMessageSendingOperations messageSendingOperations;


    @MessageMapping("chat/message")
    public void message(ChatMessage message, @Header(value = "access_token",defaultValue = "") String token){

        System.out.println(message.getChatRoomId());
        System.out.println(message.getId());

        //messageSendingOperations.convertAndSend("/sub/chat/room/"+message.getRoomId(),message);
    }

}
