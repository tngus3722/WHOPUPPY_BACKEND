package com.whopuppy.domain.criteria;

import io.swagger.annotations.ApiParam;

import javax.validation.constraints.NotNull;

public class ArticleCriteria extends  BaseCriteria{
    @ApiParam(required = false, defaultValue = "서울")
    private String region = null;
    @ApiParam(required = false)
    private String title = null;
    @NotNull
    @ApiParam(required = true)
    private Long boardId = Long.valueOf(1);

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Long getBoardId() {
        return boardId;
    }

    public void setBoardId(Long boardId) {
        this.boardId = boardId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
