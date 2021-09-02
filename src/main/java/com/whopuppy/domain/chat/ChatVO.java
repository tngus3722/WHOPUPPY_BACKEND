package com.whopuppy.domain.chat;


import com.whopuppy.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ChatVO {
    private List<ChatRoom> chatRooms;
    private List<User> users;
}
