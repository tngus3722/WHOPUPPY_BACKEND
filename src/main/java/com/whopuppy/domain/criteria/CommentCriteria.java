package com.whopuppy.domain.criteria;

import io.swagger.annotations.ApiParam;

import javax.validation.constraints.NotNull;

public class CommentCriteria extends  BaseCriteria{
    @NotNull
    @ApiParam(required = true)
    private Long article_id = Long.valueOf(1);

    public Long getArticle_id() {
        return article_id;
    }

    public void setArticle_id(Long article_id) {
        this.article_id = article_id;
    }
}
