package com.whopuppy.domain;

import org.json.simple.JSONObject;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class AnimalDTO {
    private Long idx;
    private String sexCd;
    private String kindCd;
    private String desertionNo;
    private String noticeNo;
    private String careAddr;
    private String processState;
    private String noticeSdt;
    private String weight;
    private String chargeNm;
    private String careNm;
    private String careTel;
    private String happenPlace;
    private String officetel;
    private String orgNm;
    private String filename;
    private String popfile;
    private String noticeEdt;
    private String neuterYn;
    private String specialMark;
    private String colorCd;
    private String happenDt;
    private String age;

    public Long getIdx() {
        return idx;
    }

    public void setIdx(Long idx) {
        this.idx = idx;
    }

    public AnimalDTO(){}

    public AnimalDTO(JSONObject obj) {
        this.sexCd = obj.get("sexCd").toString();
        this.kindCd = obj.get("kindCd").toString();
        this.noticeNo = obj.get("noticeNo").toString();
        this.careAddr = obj.get("careAddr").toString();
        this.processState = obj.get("processState").toString();
        this.noticeSdt = obj.get("noticeSdt").toString();
        this.weight = obj.get("weight").toString();
        this.chargeNm = Objects.toString(obj.get("chargeNm"), "");
        this.careNm = obj.get("careNm").toString();
        this.desertionNo = obj.get("desertionNo").toString();
        this.careTel = obj.get("careTel").toString();
        this.happenPlace = obj.get("happenPlace").toString();
        this.officetel = obj.get("officetel").toString();
        this.orgNm = obj.get("orgNm").toString();
        this.filename = obj.get("filename").toString();
        this.popfile = obj.get("popfile").toString();
        this.noticeEdt = obj.get("noticeEdt").toString();
        this.neuterYn = obj.get("neuterYn").toString();
        this.specialMark = obj.get("specialMark").toString();
        this.colorCd = obj.get("colorCd").toString();
        this.happenDt = obj.get("happenDt").toString();
        this.age = obj.get("age").toString();
    }

    public String getSexCd() {
        return sexCd;
    }

    public void setSexCd(String sexCd) {
        this.sexCd = sexCd;
    }

    public String getKindCd() {
        return kindCd;
    }

    public void setKindCd(String kindCd) {
        this.kindCd = kindCd;
    }

    public String getNoticeNo() { return noticeNo; }

    public void setNoticeNo(String noticeNo) { this.noticeNo = noticeNo; }

    public String getCareAddr() {
        return careAddr;
    }

    public void setCareAddr(String careAddr) {
        this.careAddr = careAddr;
    }

    public String getProcessState() {
        return processState;
    }

    public void setProcessState(String processState) {
        this.processState = processState;
    }

    public String getNoticeSdt() {
        return noticeSdt;
    }

    public void setNoticeSdt(String noticeSdt) {
        this.noticeSdt = noticeSdt;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getChargeNm() {
        return chargeNm;
    }

    public void setChargeNm(String chargeNm) {
        this.chargeNm = chargeNm;
    }

    public String getCareNm() {
        return careNm;
    }

    public void setCareNm(String careNm) {
        this.careNm = careNm;
    }

    public String getDesertionNo() {
        return desertionNo;
    }

    public void setDesertionNo(String desertionNo) {
        this.desertionNo = desertionNo;
    }

    public String getCareTel() {
        return careTel;
    }

    public void setCareTel(String careTel) {
        this.careTel = careTel;
    }

    public String getHappenPlace() {
        return happenPlace;
    }

    public void setHappenPlace(String happenPlace) {
        this.happenPlace = happenPlace;
    }

    public String getOfficetel() {
        return officetel;
    }

    public void setOfficetel(String officetel) {
        this.officetel = officetel;
    }

    public String getOrgNm() {
        return orgNm;
    }

    public void setOrgNm(String orgNm) {
        this.orgNm = orgNm;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getPopfile() {
        return popfile;
    }

    public void setPopfile(String popfile) {
        this.popfile = popfile;
    }

    public String getNoticeEdt() {
        return noticeEdt;
    }

    public void setNoticeEdt(String noticeEdt) {
        this.noticeEdt = noticeEdt;
    }

    public String getNeuterYn() {
        return neuterYn;
    }

    public void setNeuterYn(String neuterYn) {
        this.neuterYn = neuterYn;
    }

    public String getSpecialMark() {
        return specialMark;
    }

    public void setSpecialMark(String specialMark) {
        this.specialMark = specialMark;
    }

    public String getColorCd() {
        return colorCd;
    }

    public void setColorCd(String colorCd) {
        this.colorCd = colorCd;
    }

    public String getHappenDt() {
        return happenDt;
    }

    public void setHappenDt(String happenDt) {
        this.happenDt = happenDt;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}
