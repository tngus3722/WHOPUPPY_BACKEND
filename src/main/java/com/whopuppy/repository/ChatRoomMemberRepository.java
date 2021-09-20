package com.whopuppy.repository;

import com.whopuppy.domain.chat.ChatRoomMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRoomMemberRepository extends JpaRepository<ChatRoomMember, Long> {
    public ChatRoomMember findByUserIdAndChatRoomId(Long userId, Long chatRoomId);
    public List<ChatRoomMember> findAllByChatRoomId(Long chatRoomId);
    public Long countByChatRoomIdAndMessageId(Long chatRoomId, Long messageId);
}
