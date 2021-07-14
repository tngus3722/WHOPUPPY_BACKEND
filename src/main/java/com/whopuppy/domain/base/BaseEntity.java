package com.whopuppy.domain.base;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.whopuppy.annotation.ValidationGroups;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.sql.Timestamp;

@Getter
@Setter
@MappedSuperclass
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(hidden = true)
    @NotNull( groups = { ValidationGroups.Update.class}, message = "아이디는 공백일 수 없습니다.")
    @Null( groups = { ValidationGroups.Create.class}, message = "아이디는 공백이어야 합니다.")
    private Long id;

    @Column(name="is_deleted")
    @ApiModelProperty(hidden = true)
    @JsonProperty("isDeleted")
    private boolean isDeleted;

    @CreationTimestamp
    @Column(name = "created_at",updatable = false)
    @ApiModelProperty(hidden = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at",updatable = false)
    @ApiModelProperty(hidden = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Timestamp updatedAt;

}