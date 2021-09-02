package com.whopuppy.controller.chat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.whopuppy.annotation.ValidationGroups;
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
import org.springframework.validation.annotation.Validated;

@Controller
public class ChatMessageController {
    @Autowired
    @Qualifier("stompChatServiceImpl")
    private ChatService chatService;

    @Autowired
    SimpMessageSendingOperations messageSendingOperations;

    //메시지 send 컨트롤러
    @MessageMapping("chat/message")
    public void message(@Validated(ValidationGroups.Send.class) ChatMessage message, @Header(value = "Authorization",defaultValue = "") String token) throws JsonProcessingException {
//    public void message(ChatMessage message){

        System.out.println(message.getMessage());
        System.out.println(message.getChatRoomId());
        System.out.println(token);
        chatService.sendMessage(message, token);
        //messageSendingOperations.convertAndSend("/sub/chat/room/"+message.getRoomId(),message);
    }

    //메시지 읽음처리
    @MessageMapping("chat/read")
    public void read(Long id, @Header(value = "Authorization",defaultValue = "") String token) throws JsonProcessingException {

        System.out.println(token);
        chatService.readMessage(id, token);
        //messageSendingOperations.convertAndSend("/sub/chat/room/"+message.getRoomId(),message);
    }
}
