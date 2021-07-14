package com.whopuppy.controller.chat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.whopuppy.domain.chat.ChatMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import javax.annotation.Resource;
import java.io.IOException;

@RestController("KafkaSampleController")
@RequestMapping(value = "/chat")
public class ChatKafkaController {
    @Resource
    KafkaTemplate<String,String> kafkaProducer;

    @GetMapping(value = "/test/send")
    public ResponseEntity<String> test() throws JsonProcessingException {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setMessage("메시지");
        chatMessage.setSendUserId(1L);
        chatMessage.setChatRoomId(1L);
        kafkaProducer.send("TOPIC", new ObjectMapper().writeValueAsString(chatMessage));
        return new ResponseEntity<String>("", HttpStatus.OK);
    }

    @KafkaListener(topics = "TOPIC", groupId = "test")
    public void consume(String message) throws IOException {
        System.out.println("스트링 : " + String.format("Consumed message : %s", message));
        ChatMessage chatMessage = new ObjectMapper().readValue(message,ChatMessage.class);
        System.out.println("클래스 : " + chatMessage.toString());
    }
}
