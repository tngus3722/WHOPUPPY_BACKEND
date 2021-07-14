package com.whopuppy.domain.chat;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.whopuppy.domain.base.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name="chat_message")
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatMessage extends BaseEntity {

    @Column(name="chat_room_id")
    private Long chatRoomId;

    @Column(name="send_user_id")
    private Long sendUserId;

    @Column(name="message",columnDefinition = "TEXT")
    private String message;

    @Column(name="read_count")
    @ApiModelProperty(hidden = true)
    private Integer readCount;

}