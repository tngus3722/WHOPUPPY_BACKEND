package com.whopuppy.controller.chat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.whopuppy.annotation.Auth;
import com.whopuppy.annotation.ValidationGroups;
import com.whopuppy.domain.chat.ChatMessage;
import com.whopuppy.domain.chat.ChatRoom;
import com.whopuppy.service.ChatService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import javax.annotation.Resource;
import java.io.IOException;

@RestController("KafkaSampleController")
@RequestMapping(value = "/chat")
public class ChatKafkaController {
    @Resource
    KafkaTemplate<String,String> kafkaProducer;


    @Resource
    @Qualifier("stompChatServiceImpl")
    private ChatService chatService;

    @Auth(authority = Auth.Authority.NONE, role = Auth.Role.NORMAL)
    @ApiOperation(value = "채팅방 목록 출력", notes = "채팅방 목록 출력", authorizations = @Authorization(value = "Bearer +accessToken"))
    @PostMapping(value = "/send")
    public ResponseEntity<String> test(@RequestBody ChatMessage chatMessage) throws JsonProcessingException {
        System.out.println("카프카 센드 태스트 ");
        kafkaProducer.send("TOPIC", new ObjectMapper().writeValueAsString(chatMessage));
        return new ResponseEntity<String>("", HttpStatus.OK);
    }

    @KafkaListener(topics = "TOPIC", groupId = "test")
    public void consume(String message) throws IOException {
        System.out.println("스트링 : " + String.format("Consumed message : %s", message));
        ChatMessage chatMessage = new ObjectMapper().readValue(message,ChatMessage.class);
        //System.out.println("클래스 : " + chatMessage.toString());
        chatService.spreadMessage(chatMessage);

    }
}
