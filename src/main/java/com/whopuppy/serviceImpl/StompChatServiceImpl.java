package com.whopuppy.serviceImpl;

import com.whopuppy.annotation.ValidationGroups;
import com.whopuppy.domain.chat.ChatMessage;
import com.whopuppy.domain.chat.ChatRoom;
import com.whopuppy.domain.chat.ChatRoomMember;
import com.whopuppy.domain.criteria.ChatRoomCriteria;
import com.whopuppy.domain.user.User;
import com.whopuppy.mapper.ChatRoomMapper;

import com.whopuppy.mapper.ChatRoomMemberMapper;
import com.whopuppy.mapper.UserMapper;
import com.whopuppy.repository.ChatRoomMemberRepository;
import com.whopuppy.repository.ChatRoomRepository;
import com.whopuppy.service.ChatService;
import com.whopuppy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.validation.SmartValidator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.socket.WebSocketSession;


import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.*;

@Service
public class StompChatServiceImpl implements ChatService {

    @Autowired
    @Qualifier("UserServiceImpl")
    private UserService userService;

    @Resource
    private UserMapper userMapper;

    @Resource
    private ChatRoomRepository chatRoomRepo;

    @Resource
    private ChatRoomMemberRepository chatRoomMemberRepo;

    @Resource
    private ChatRoomMapper chatRoomMapper;

    @Resource
    private ChatRoomMemberMapper chatRoomMemberMapper;

    @Autowired
    SimpMessageSendingOperations messageSendingOperations;

    @Override
    @Transactional(readOnly = true)
    public ChatRoomMember findRoomUser(Long roomId, Long userId) {
        return chatRoomMemberMapper.getChatRoomMember(roomId,userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChatRoom> findAllRoom() {
        //페이지네이션 적용 X
        System.out.println(userService.getLoginUserId());

        return chatRoomMapper.getChatRooms(userService.getLoginUserId());
    }

    @Override
    @Transactional(readOnly = true)
    public ChatRoom findRoomById(Long roomId) {
        return chatRoomMapper.getChatRoom(roomId);
    }

    @Override
    @Transactional(readOnly = false)
    public ChatRoom createRoom(ChatRoom chatRoom) {
        //userList 검증 필요
        Long userId = userService.getLoginUserId();

        //성공 실패에 대한 처리 X

        chatRoom.setCreateUserId(userId);
        chatRoom.setMemberCount(0);
        chatRoomRepo.save(chatRoom);

        List<ChatRoomMember> chatRoomMemberList = new ArrayList<>();
        chatRoomMemberList.add(new ChatRoomMember(
            chatRoom.getId(),0L,userId,true,true));
        for(User user : chatRoom.getUsers()){
            chatRoomMemberList.add(new ChatRoomMember(
                chatRoom.getId(),0L,user.getId(),false,false));
        }
        chatRoom.setMemberCount(chatRoomMemberList.size());
        chatRoomRepo.save(chatRoom);
        chatRoomMemberRepo.saveAll(chatRoomMemberList);
        return chatRoom;
    }

    @Transactional(readOnly = false)
    @Override
    public void sendMessage(ChatMessage message, String token) {

        //messageSendingOperations.convertAndSend("/sub/chat/users/"+message.getRoomId(),message);
    }

    @Override
    public boolean isBelong(String token) {
        return true;
    }

    @Override
    @Transactional(readOnly = false)
    public boolean invite(Long chatRoomId, Long targetUserId)
    {
        System.out.println(TransactionSynchronizationManager.getCurrentTransactionName());
        return false;
    }

    @Override
    public boolean invite(User user, Long chatRoomId, Long targetUserId) {
        // 유저 아이디 확인
        System.out.println(TransactionSynchronizationManager.getCurrentTransactionName());
        return invite(chatRoomId, targetUserId);
    }

    @Override
    public List<ChatRoomMember> test(Timestamp timestamp) {
        return chatRoomMemberMapper.test(timestamp);
    }
}
