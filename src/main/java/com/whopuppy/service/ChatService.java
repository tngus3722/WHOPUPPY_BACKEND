package com.whopuppy.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.whopuppy.domain.chat.ChatMessage;
import com.whopuppy.domain.chat.ChatRoom;
import com.whopuppy.domain.chat.ChatRoomMember;
import com.whopuppy.domain.chat.ChatVO;
import com.whopuppy.domain.criteria.ChatRoomCriteria;
import com.whopuppy.domain.user.User;
import org.springframework.web.socket.WebSocketSession;


import java.sql.Timestamp;
import java.util.List;

public interface ChatService {
    public ChatRoomMember findRoomUser(Long roomId, Long userId);
    public ChatVO findAllRoom();

    public ChatRoom findRoomById(Long roomId);

    public ChatRoom createRoom(ChatRoom chatRoom);

    public default <T> void sendMessage(WebSocketSession session, T message){};

    public void sendMessage(ChatMessage message,String token) throws JsonProcessingException;

    public boolean isBelong(String token);

    public boolean invite(Long chatRoomId, Long targetUserId);
    public boolean invite(User user, Long chatRoomId, Long targetUserId);

    public void spreadMessage(ChatMessage message);
    //테스트용 나중에 지워
    public List<ChatRoomMember> test(Timestamp valueOf);

    List<ChatMessage> findChatMessages(Long roomId, Long id, Integer count);

    void readMessage(Long id, String token);

    ChatRoom test2(Long id);
}
