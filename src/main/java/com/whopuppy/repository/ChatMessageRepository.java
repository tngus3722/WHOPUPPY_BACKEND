package com.whopuppy.repository;

import com.whopuppy.domain.chat.ChatMessage;
import com.whopuppy.domain.chat.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

}
