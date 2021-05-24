package com.whopuppy.domain.snack;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import java.sql.Timestamp;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SnackImage {
    private Long id;
    private Long snack_id;
    private String image_url;
    @ApiModelProperty(hidden = true)
    private boolean is_deleted;
    @ApiModelProperty(hidden = true)
    private Timestamp created_at;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSnack_id() {
        return snack_id;
    }

    public void setSnack_id(Long snack_id) {
        this.snack_id = snack_id;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
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
}
