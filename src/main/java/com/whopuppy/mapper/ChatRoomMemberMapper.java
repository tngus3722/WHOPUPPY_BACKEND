package com.whopuppy.mapper;

import com.whopuppy.domain.chat.ChatRoomMember;
import com.whopuppy.domain.criteria.BaseCriteria;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;
import java.util.List;

@Mapper
public interface ChatRoomMemberMapper {
    //채팅방 하나 선택
    public ChatRoomMember getChatRoomMember(@Param("room_id") Long roomId, @Param("user_id") Long userId);

    //
    public List<ChatRoomMember> getChatRoomMembers(@Param("room_id") Long roomId, BaseCriteria baseCriteria);

    //나중에 없앨 것
    List<ChatRoomMember> test(@Param("time") Timestamp timestamp);

    void setReadMessage(@Param("user_id") Long userId, @Param("room_id") Long chatRoomId, @Param("to") Long to);

    void updateMessageId(@Param("chat_room_id")Long chatRoomId, @Param("from")Long  from,@Param("to") Long to);
}
