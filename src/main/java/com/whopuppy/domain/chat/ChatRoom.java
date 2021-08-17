package com.whopuppy.domain.chat;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.whopuppy.annotation.ValidationGroups;
import com.whopuppy.domain.base.BaseEntity;
import com.whopuppy.domain.community.Article;
import com.whopuppy.domain.user.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

import java.util.List;

@Entity
@Table(name="chat_room")
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatRoom extends BaseEntity {

    @Column(name="article_id")
    private Long articleId;

    @Column(name="create_user_id")
    @NotNull( groups = { ValidationGroups.Update.class}, message = "생성자는 공백일 수 없습니다.")
    @Null( groups = { ValidationGroups.Create.class}, message = "생성자는 공백이어야 합니다.")
    private Long createUserId;

    @Column(name="member_count")
    @ApiModelProperty(hidden = true)
    private Integer memberCount;

    @Column(name="name")
    @NotNull( groups = { ValidationGroups.Create.class,ValidationGroups.Update.class}, message = "방 이름은 공백일 수 없습니다.")
    private String name;

    @Transient
    @ApiModelProperty(hidden = true)
    //@NotNull( groups = { ValidationGroups.Create.class}, message = "참가자는 공백일 수 없습니다.")
    //@Size(groups = { ValidationGroups.Create.class}, min = 1, message = "1명 이상의 참가자가 필요합니다(2)")
    private List<User> users;

    @Transient
    @ApiModelProperty(hidden = true)
    private Article article;

    @Transient
    //최근 메시지를 넣어주기 위한 공간
    @ApiModelProperty(hidden = true)
    private ChatMessage chatMessage;

    @Transient
    //메시지 수 카운트
    @ApiModelProperty(hidden = true)
    private Long messageCount;
    /*
    @Transient
    @ApiModelProperty(hidden = true)
    @NotNull( groups = { ValidationGroups.Create.class}, message = "참가자는 공백일 수 없습니다.")
    @Size(groups = { ValidationGroups.Create.class}, min = 1, message = "1명 이상의 참가자가 필요합니다(2)")
    private List<Long> uidList;
    */

}
