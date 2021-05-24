package com.whopuppy.domain.user;

import com.whopuppy.annotation.ValidationGroups;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Timestamp;

@Component
public class
AuthNumber {
    @ApiModelProperty(hidden = true)
    private Long id;


    @Size( min=6, max = 30,groups = { ValidationGroups.authority.class}, message = "아이디는 6글자에서 30글자입니다")
    @NotNull( groups = { ValidationGroups.authority.class}, message = "아이디는 공백일 수 없습니다.")
    private String account;

    @NotNull( groups = { ValidationGroups.configSms.class,  ValidationGroups.sendSms.class}, message = "번호는 필수입니다.")
    @Size( min=11, max = 11,groups ={ ValidationGroups.configSms.class,  ValidationGroups.sendSms.class}, message = "핸드폰번호는 11글자입니다.")
    private String phone_number;
    @NotNull( groups = { ValidationGroups.authority.class,ValidationGroups.configSms.class,  ValidationGroups.sendSms.class}, message = "flag 값은 필수입니다.")
    @Min( value = 0,groups = { ValidationGroups.configSms.class,  ValidationGroups.sendSms.class}, message = "번호 인증의 flag는 0 보다 작을 수 없습니다.")
    @Max(value = 1,groups = { ValidationGroups.configSms.class,  ValidationGroups.sendSms.class}, message = "번호 인증의 flag는 1 보다 클 수 없습니다.")
    @Min( value = 1,groups = {ValidationGroups.authority.class}, message = "권한부여의 flag는 1 보다 작을 수 없습니다.")
    @Max(value = 4,groups = { ValidationGroups.authority.class}, message = "권한부여의 flag는 4 보다 클 수 없습니다.")
    private Integer flag;
    @Size( min=6, max = 6,groups = ValidationGroups.configSms.class, message = "인증 키 값은 6글자입니다.")
    @NotNull( groups = ValidationGroups.configSms.class, message = "인증 키 값은 null일 수 없습니다.")
    private String secret;

    @Size(min = 11, max = 11, groups = {  ValidationGroups.sendSms.class}, message =  "해쉬코드의 길이는 11자입니다.")
    @NotNull( groups = {  ValidationGroups.sendSms.class}, message = "해쉬코드는 공백일 수 없습니다.")
    private String code;

    @ApiModelProperty(hidden = true)
    private String ip;
    @ApiModelProperty(hidden = true)
    private boolean is_authed;
    @ApiModelProperty(hidden = true)
    private Timestamp expired_at;
    @ApiModelProperty(hidden = true)
    private Timestamp created_at;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public boolean isIs_authed() {
        return is_authed;
    }

    public void setIs_authed(boolean is_authed) {
        this.is_authed = is_authed;
    }

    public Timestamp getExpired_at() {
        return expired_at;
    }

    public void setExpired_at(Timestamp expired_at) {
        this.expired_at = expired_at;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }
}
