package com.whopuppy.domain.criteria;

import io.swagger.annotations.ApiParam;

import javax.validation.constraints.NotNull;

public class AnimalListCriteria extends  BaseCriteria{
    @NotNull
    @ApiParam
    private String noticeNo;

    @ApiParam
    private String sexCd;

    @ApiParam
    private String kindCd;

    public String getNoticeNo() {
        return noticeNo;
    }
    public String getSexCd() { return sexCd; }
    public String getKindCd() { return kindCd; }

    public void setNoticeNo(String noticeNo) {
        this.noticeNo = noticeNo;
    }
    public void setSexCd(String sexCd) { this.sexCd = sexCd; }
    public void setKindCd(String kindCd) { this.kindCd = kindCd; }
}
