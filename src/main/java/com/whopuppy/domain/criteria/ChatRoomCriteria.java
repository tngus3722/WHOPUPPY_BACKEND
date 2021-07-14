package com.whopuppy.domain.criteria;

import io.swagger.annotations.ApiParam;

import javax.validation.constraints.NotNull;

public class ChatRoomCriteria extends  BaseCriteria{
    @ApiParam(required = false, defaultValue = "")
    private String title;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "ChatRoomCriteria{" +
            "title='" + title + '\'' +
            '}';
    }
}
