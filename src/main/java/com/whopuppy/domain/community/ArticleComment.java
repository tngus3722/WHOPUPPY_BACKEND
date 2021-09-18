package com.whopuppy.domain.community;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.whopuppy.annotation.ValidationGroups;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Timestamp;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArticleComment {

    @ApiModelProperty(hidden = true)
    private Long id;
    @JsonProperty("article_id")
    @ApiModelProperty(hidden = true)
    private Long articleId;
    @JsonProperty("user_id")
    @ApiModelProperty(hidden = true)
    private Long userId;
    @JsonProperty("user_nickname")
    @ApiModelProperty(hidden = true)
    private String userNickname;
    @JsonProperty("user_profile_image")
    @ApiModelProperty(hidden = true)
    private String userProfileImage;

    @NotNull(groups = ValidationGroups.postComment.class, message = "내용은 필수 값입니다.")
    @Size(min = 1, max = 50 ,groups = ValidationGroups.postComment.class, message = "1글자부터 50글자 까지 필수 값입니다.")
    private String content;

    @ApiModelProperty(hidden = true)
    @JsonProperty("is_deleted")
    private boolean isDeleted;
    @ApiModelProperty(hidden = true)
    @JsonProperty("created_at")
    private Timestamp createdAt;
    @JsonProperty("updated_at")
    @ApiModelProperty(hidden = true)
    private Timestamp updatedAt;
}
