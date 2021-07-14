package com.whopuppy.domain.chat;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.whopuppy.domain.base.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.sql.Timestamp;
@Entity
@Table(name="chat_room_member")
@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatRoomMember extends BaseEntity {

    @Column(name="chat_room_id")
    private Long chatRoomId;

    @Column(name="message_id")
    private Long messageId;

    @Column(name="user_id")
    private Long userId;

    @Column(name="is_create")
    @JsonProperty("isCreate")
    private boolean isCreate;

    @Column(name="is_admin")
    @JsonProperty("isAdmin")
    private boolean isAdmin;

    public ChatRoomMember(Long chatRoomId, Long messageId, Long userId, boolean isCreate, boolean isAdmin) {
        this.chatRoomId = chatRoomId;
        this.messageId = messageId;
        this.userId = userId;
        this.isCreate = isCreate;
        this.isAdmin = isAdmin;
    }

    @Transient
    @ApiModelProperty(hidden = true)
    private ChatMessage chatMessage;
}