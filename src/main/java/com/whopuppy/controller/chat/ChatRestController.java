package com.whopuppy.controller.chat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.whopuppy.annotation.Auth;
import com.whopuppy.annotation.ValidationGroups;
import com.whopuppy.domain.chat.ChatMessage;
import com.whopuppy.domain.chat.ChatRoom;
import com.whopuppy.domain.chat.ChatRoomMember;
import com.whopuppy.domain.chat.ChatVO;
import com.whopuppy.domain.criteria.ChatRoomCriteria;
import com.whopuppy.mapper.ChatRoomMapper;
import com.whopuppy.service.ChatService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(value = "/chat")
public class ChatRestController {
    @Autowired
    @Qualifier("stompChatServiceImpl")
    private ChatService chatService;

    @Autowired
    HttpServletRequest request;


    // 모든 채팅방 목록 반환
    @GetMapping("/rooms")
    @Auth(authority = Auth.Authority.NONE, role = Auth.Role.NORMAL)
    @ApiOperation(value = "채팅방 목록 출력", notes = "채팅방 목록 출력", authorizations = @Authorization(value = "Bearer +accessToken"))
    public ResponseEntity<ChatVO> room()
    {
        return new ResponseEntity<ChatVO>(chatService.findAllRoom(),HttpStatus.OK);
    }

    // 채팅방 생성
    @PostMapping("/room")
    @Auth(authority = Auth.Authority.NONE, role = Auth.Role.NORMAL)
    @ApiOperation(value = "채팅방 생성", notes = "채팅방 생성", authorizations = @Authorization(value = "Bearer +accessToken"))
    public ResponseEntity<ChatRoom> createRoom(@RequestBody @Validated(ValidationGroups.Create.class) ChatRoom chatRoom){
        return new ResponseEntity<ChatRoom>(chatService.createRoom(chatRoom), HttpStatus.CREATED);
    }
    // 특정 채팅방 조회
    @GetMapping("/room/{roomId}")
    @Auth(authority = Auth.Authority.NONE, role = Auth.Role.NORMAL)
    @ApiOperation(value = "특정 채팅방 조회", notes = "채팅방 조회", authorizations = @Authorization(value = "Bearer +accessToken"))
    public ChatRoom roomInfo(@PathVariable Long roomId) {
        return chatService.findRoomById(roomId);
    }

    // 최근 메시지 가져오기
    @GetMapping("/room/{roomId}/message")
    @Auth(authority = Auth.Authority.NONE, role = Auth.Role.NORMAL)
    @ApiOperation(value = "최근 메시지 가져오기", notes = "최근 메시지 조회", authorizations = @Authorization(value = "Bearer +accessToken"))
    public List<ChatMessage> getChatMessage(@PathVariable Long roomId
        , @ApiParam(value = "(required: id), example = 1") @RequestParam(value="id",required = true) Long id //message ID
        , @ApiParam(value = "(required: count), example = 20") @RequestParam(value="count",required = true, defaultValue = "20") Integer count // 개수
    ) {
        return chatService.findChatMessages(roomId, id, count);
    }

    // 특정 채팅방-유저 조회
    @GetMapping("/room/{roomId}/user/{userId}")
    //@Auth(authority = Auth.Authority.NONE, role = Auth.Role.NORMAL)
    //@ApiOperation(value = "특정 채팅방 조회", notes = "채팅방 조회", authorizations = @Authorization(value = "Bearer +accessToken"))
    public ResponseEntity<ChatRoomMember> roomInfo(@PathVariable Long roomId, @PathVariable Long userId) {
        return new ResponseEntity<ChatRoomMember>(chatService.findRoomUser(roomId, userId),HttpStatus.OK);
    }

    @GetMapping("/read")
    @ApiOperation(value = "메시지 읽음처리", notes = "메시지 읽음처리", authorizations = @Authorization(value = "Bearer +accessToken"))
    public void read(Long id) throws JsonProcessingException {
        String token = request.getHeader("Authorization");

        System.out.println(token);
        chatService.readMessage(id, token);
        //messageSendingOperations.convertAndSend("/sub/chat/room/"+message.getRoomId(),message);
    }
}
