package com.whopuppy.mapper;

import com.whopuppy.domain.chat.ChatMessage;
import com.whopuppy.domain.chat.ChatRoomMember;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;
import java.util.List;

@Mapper
public interface ChatMessageMapper {
    public List<ChatMessage> getChatMessageList(@Param("room_id") Long roomId, @Param("pre_chat_id") Long preChatId
    , int size);

    public ChatMessage getChatMessage(@Param("id") Long chatMessageId);


}
