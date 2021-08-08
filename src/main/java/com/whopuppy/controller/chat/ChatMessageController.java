package com.whopuppy.controller.chat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.whopuppy.domain.chat.ChatMessage;
import com.whopuppy.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
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

//  pub/chat/message
    @MessageMapping("chat/message")
    public void message(ChatMessage message, @Header(value = "Authorization",defaultValue = "") String token) throws JsonProcessingException {
//    public void message(ChatMessage message){

        System.out.println(message.getMessage());
        System.out.println(message.getChatRoomId());
        System.out.println(token);
        chatService.sendMessage(message, token);
        //messageSendingOperations.convertAndSend("/sub/chat/room/"+message.getRoomId(),message);
    }

}
