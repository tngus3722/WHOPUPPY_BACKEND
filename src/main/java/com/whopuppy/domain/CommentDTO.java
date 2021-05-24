package com.whopuppy.domain;

import com.whopuppy.annotation.ValidationGroups;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Time;
import java.sql.Timestamp;

@Component
public class CommentDTO {

    @ApiModelProperty(hidden = true)
    private Long id;

    @NotNull( groups = { ValidationGroups.animalComment.class}, message = "게시글 번호는 공백일 수 없습니다.")
    private Long article_id;

    @NotNull( groups = { ValidationGroups.animalComment.class}, message = "작성자는 공백일 수 없습니다.")
    private Long user_id;

    @Size( max = 200,groups = { ValidationGroups.animalComment.class}, message = "댓글 내용은 최대 100글자입니다")
    @NotNull( groups = { ValidationGroups.animalComment.class}, message = "댓글 내용은 공백일 수 없습니다.")
    private String content;

    @ApiModelProperty(hidden = true)
    private Boolean is_deleted;

    @ApiModelProperty(hidden = true)
    private Timestamp created_at;

    @ApiModelProperty(hidden = true)
    private Timestamp updated_at;

    public Long getId(){ return id;}

    public Long getArticle_id(){ return article_id;}

    public Long getUser_id(){ return user_id;}

    public String getContent(){ return content;}

    public Boolean getIs_deleted() { return is_deleted; }

    public Timestamp getCreated_at() {return created_at;}

    public Timestamp getUpdated_at() {return updated_at;}

    public void setId(Long id){ this.id = id; }
    public void setArticle_id(Long article_id){ this.article_id = article_id; }
    public void setContent(String content){ this.content = content; }
    public void setUser_id(Long user_id){ this.user_id = user_id; }
}
