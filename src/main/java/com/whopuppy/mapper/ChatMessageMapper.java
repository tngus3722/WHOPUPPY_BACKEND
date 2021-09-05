package com.whopuppy.mapper;

import com.whopuppy.domain.chat.ChatMessage;
import com.whopuppy.domain.chat.ChatRoomMember;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;
import java.util.List;

@Mapper
public interface ChatMessageMapper {
    public List<ChatMessage> getChatMessageList(@Param("room_id") Long roomId, @Param("chat_id") Long preChatId
    , @Param("size") int size);

    public ChatMessage getChatMessage(@Param("id") Long chatMessageId);


    void setReadMessage(@Param("room_id") Long chatRoomId, @Param("from") Long from, @Param("to") Long to);
}
