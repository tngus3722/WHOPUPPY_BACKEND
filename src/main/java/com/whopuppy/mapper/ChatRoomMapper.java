package com.whopuppy.mapper;

import com.whopuppy.domain.chat.ChatRoom;
import com.whopuppy.domain.user.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ChatRoomMapper {
    //채팅방 검색
    public List<ChatRoom> getChatRooms(@Param("user_id") Long userId);

    //채팅방 하나 선택
    public ChatRoom getChatRoom(@Param("id") Long id);

    public List<User> getChatRoomMembers(@Param("chatRooms") List<ChatRoom> chatRooms);

}
