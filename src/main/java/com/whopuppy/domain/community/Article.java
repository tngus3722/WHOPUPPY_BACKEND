package com.whopuppy.domain.community;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.whopuppy.annotation.ValidationGroups;
import com.whopuppy.domain.base.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.List;
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Article extends BaseEntity {

    private Long board_id;
    @ApiModelProperty(hidden = true)
    private String board;
    @ApiModelProperty(hidden = true)
    private Long user_id;
    @ApiModelProperty(hidden = true)
    private String nickname;
    @ApiModelProperty(hidden = true)
    private String profile_image_url;

    @Size( min = 1, max =50 ,groups = ValidationGroups.postCommunity.class, message = "제목은 50글자 까지 입니다.")
    private String title;
    @Size( min = 1, max =500 ,groups = ValidationGroups.postCommunity.class, message = "게시글은 500글자 까지 입니다.")
    private String content;
    private List<@Length(min=40, message = "url은 최소 40글자 이상 " )
                 @NotNull(message = "이미지는 null 일 수 없습니다") String> images;

    @NotNull( groups = ValidationGroups.postCommunity.class, message = "지역 선택은 필수 입니다.")
    private String region;

    @ApiModelProperty(hidden = true)
    private String thumbnail;


}
