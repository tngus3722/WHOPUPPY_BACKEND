package com.whopuppy.serviceImpl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.whopuppy.annotation.ValidationGroups;
import com.whopuppy.domain.chat.ChatMessage;
import com.whopuppy.domain.chat.ChatRoom;
import com.whopuppy.domain.chat.ChatRoomMember;
import com.whopuppy.domain.chat.ChatVO;
import com.whopuppy.domain.criteria.ChatRoomCriteria;
import com.whopuppy.domain.user.User;
import com.whopuppy.enums.ErrorMessage;
import com.whopuppy.exception.CriticalException;
import com.whopuppy.exception.NonCriticalException;
import com.whopuppy.mapper.ChatMessageMapper;
import com.whopuppy.mapper.ChatRoomMapper;

import com.whopuppy.mapper.ChatRoomMemberMapper;
import com.whopuppy.mapper.UserMapper;
import com.whopuppy.repository.ChatMessageRepository;
import com.whopuppy.repository.ChatRoomMemberRepository;
import com.whopuppy.repository.ChatRoomRepository;
import com.whopuppy.service.ChatService;
import com.whopuppy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
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

    @Resource
    KafkaTemplate<String,String> kafkaProducer;

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
    private ChatMessageMapper chatMessageMapper;

    @Resource
    private ChatMessageRepository chatMessageRepo;
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
    public ChatVO findAllRoom() {
        //페이지네이션 적용 X
        System.out.println(userService.getLoginUserId());
        List<ChatRoom> chatRooms = chatRoomMapper.getChatRooms(userService.getLoginUserId());
        if(chatRooms==null || chatRooms.size()==0)
            return new ChatVO(chatRooms, new ArrayList<User>());

        return new ChatVO(chatRooms,chatRoomMapper.getChatRoomMembers(chatRooms));
    }

    @Override
    @Transactional(readOnly = true)
    public ChatRoom findRoomById(Long roomId) {
        ChatRoom chatRoom = chatRoomMapper.getChatRoom(roomId);
        if(chatRoom==null) return null;

        List<User> users = chatRoomMapper.getChatRoomMembers(Arrays.asList(chatRoom));
        chatRoom.setUsers(users);
        return chatRoom;
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
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setSendUserId(userId);
        chatMessage.setReadCount(chatRoom.getUsers().size());
        chatMessage.setMessage("<!HELLO!>");
        chatMessage.setChatRoomId(chatRoom.getId());
        chatMessageRepo.save(chatMessage);

        List<ChatRoomMember> chatRoomMemberList = new ArrayList<>();
        chatRoomMemberList.add(new ChatRoomMember(
            chatRoom.getId(),chatMessage.getId(),userId,true,true));
        for(User user : chatRoom.getUsers()){
            chatRoomMemberList.add(new ChatRoomMember(
                chatRoom.getId(),-1L,user.getId(),false,false));
        }
        chatRoom.setMemberCount(chatRoomMemberList.size());
        chatRoomRepo.save(chatRoom);
        chatRoomMemberRepo.saveAll(chatRoomMemberList);
        return chatRoom;
    }

    @Transactional(readOnly = false)
    @Override
    public void sendMessage(ChatMessage chatMessage, String token) throws JsonProcessingException {
        User me = userService.getMe(token);

        ChatRoomMember chatRoomMember = chatRoomMemberRepo.findByUserIdAndChatRoomId(me.getId(), chatMessage.getChatRoomId());
        //나중에 에러 종류 추가
        if(chatRoomMember==null) throw new CriticalException(ErrorMessage.UNDEFINED_EXCEPTION);

        ChatRoom chatRoom = chatRoomRepo.findById(chatMessage.getChatRoomId()).orElseThrow();


        chatMessage.setSendUserId(me.getId());
        chatMessage.setReadCount(chatRoom.getMemberCount()-1); //자기 자신의 읽음 처리 추가
        chatMessageRepo.save(chatMessage);

        chatRoomMember.setMessageId(chatMessage.getId());
        chatRoomMemberRepo.save(chatRoomMember);
        chatMessage.setUser(me);

        Long count = chatRoomMemberRepo.countByChatRoomIdAndMessageId(chatMessage.getChatRoomId(), -1L);
        if(count != 0){
            chatRoomMemberMapper.updateMessageId(chatMessage.getChatRoomId(),-1L, 0L);
        }
        kafkaProducer.send("TOPIC", new ObjectMapper().writeValueAsString(chatMessage));
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
    public void spreadMessage(ChatMessage message) {
        System.out.println("스프레드");
        List<ChatRoomMember> chatRoomMemberList = chatRoomMemberRepo.findAllByChatRoomId(message.getChatRoomId());
        for(ChatRoomMember chatRoomMember : chatRoomMemberList ){
            messageSendingOperations.convertAndSend("/sub/chat/users/"+chatRoomMember.getUserId(),message);
        }

    }

    @Override
    public List<ChatRoomMember> test(Timestamp timestamp) {
        return chatRoomMemberMapper.test(timestamp);
    }

    @Override
    public List<ChatMessage> findChatMessages(Long roomId, Long id, Integer count) {
        return chatMessageMapper.getChatMessageList(roomId, id, count);
    }

    @Override
    public void readMessage(Long id, String token) {
        User me = userService.getMe(token);
        ChatMessage chatMessage = chatMessageRepo.findById(id).orElseThrow(() -> new NonCriticalException(ErrorMessage.NULL_POINTER_EXCEPTION));
        //여건 상 에러처리 추가하지 못함.
        ChatRoomMember chatRoomMember = chatRoomMemberRepo.findByUserIdAndChatRoomId(me.getId(), chatMessage.getChatRoomId());
        //Message count 낮추기
        //from (<) id (<=) to 사이의 메시지 중 room id가 동일한 것들의 read count를 -1 해준다.
        //
        // @Param("room_id") Long chatRoomId, @Param("from") Long from, @Param("to") Long to
        chatMessageMapper.setReadMessage(chatMessage.getChatRoomId(), chatRoomMember.getMessageId(), id);
        //ChatRoom Member의 메시지 값을 to로 바꾼다.
        //@Param("user_id") Long userId, @Param("room_id") Long chatRoomId, @Param("to") Long to
        chatRoomMemberMapper.setReadMessage(me.getId(), chatMessage.getChatRoomId(), id);

        //to from 찾아내서
        //해당 값 return 해주기
        //리턴의 형태를 클라이언트에서 어떻게 처리시켜야할지 몰라서
        //일단 void로 마무리 -> 읽은거 카운트 안하겠다는 의미

    }
}
