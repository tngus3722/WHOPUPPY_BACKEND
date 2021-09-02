package com.whopuppy.controller.chat;

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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import javax.annotation.Resource;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(value = "/chat")
public class ChatRestController {
    @Autowired
    @Qualifier("stompChatServiceImpl")
    private ChatService chatService;
    //삭제할것
    @Resource
    private ChatRoomMapper chatRoomMapper;

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

    // 테스트
    @PostMapping("/room/test")
    public ResponseEntity<List<ChatRoomMember>> test(@ApiParam(value = "(required: start_time), example = 2020-01-01 00:00:00") @RequestParam(value="start_time",required = true, defaultValue = "#{T(java.time.LocalDateTime).now()}") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start_date) {
        return new ResponseEntity<List<ChatRoomMember>>(chatService.test(Timestamp.valueOf(start_date)),HttpStatus.OK);
    }

    // 테스트
    @PostMapping("/room/test2")
    public ResponseEntity<List<ChatRoom>> test2() {
        return new ResponseEntity<List<ChatRoom>>(chatRoomMapper.getChatRooms(1L),HttpStatus.OK);
    }
    // 테스트
    @PostMapping("/room/test3")
    public ResponseEntity<ChatRoom> test3() {
        return new ResponseEntity<ChatRoom>(chatRoomMapper.getChatRoom(1L),HttpStatus.OK);
    }

}
