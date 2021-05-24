package com.whopuppy.domain.snack;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.whopuppy.annotation.ValidationGroups;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Size;
import java.sql.Timestamp;
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SnackArticle {
    @ApiModelProperty(hidden = true)
    private Long id;
    @ApiModelProperty(hidden = true)
    private Long user_id;

    @Size( min = 1, max =50 ,groups = ValidationGroups.postCommunity.class, message = "제목은 50글자 까지 입니다.")
    private String title;

    @ApiModelProperty(hidden = true)
    private String nickname;

    @ApiModelProperty(hidden = true)
    private String profile_image_url;
    @Size( min = 1, max =2500 ,groups = ValidationGroups.postCommunity.class, message = "게시글은 2500글자 까지 입니다.")
    private String content;

    @ApiModelProperty(hidden = true)
    private String thumbnail;
    @ApiModelProperty(hidden = true)
    private boolean is_deleted;
    @ApiModelProperty(hidden = true)
    private Timestamp created_at;
    @ApiModelProperty(hidden = true)
    private Timestamp updated_at;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getProfile_image_url() {
        return profile_image_url;
    }

    public void setProfile_image_url(String profile_image_url) {
        this.profile_image_url = profile_image_url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public boolean isIs_deleted() {
        return is_deleted;
    }

    public void setIs_deleted(boolean is_deleted) {
        this.is_deleted = is_deleted;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

    public Timestamp getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Timestamp updated_at) {
        this.updated_at = updated_at;
    }
}
