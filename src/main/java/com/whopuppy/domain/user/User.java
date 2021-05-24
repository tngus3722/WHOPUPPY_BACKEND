package com.whopuppy.domain.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.whopuppy.annotation.ValidationGroups;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Component
public class User {
    @ApiModelProperty(hidden = true)
    private Long id;

    @Size( min=6, max = 30,groups = { ValidationGroups.findPassword.class, ValidationGroups.logIn.class,  ValidationGroups.signUp.class, ValidationGroups.accountCheck.class}, message = "아이디는 6글자에서 30글자입니다")
    @NotNull( groups = { ValidationGroups.findPassword.class, ValidationGroups.logIn.class,  ValidationGroups.signUp.class, ValidationGroups.accountCheck.class}, message = "아이디는 공백일 수 없습니다.")
    private String account;

    @Size( min=64, max = 64,groups = { ValidationGroups.findPassword.class, ValidationGroups.logIn.class,  ValidationGroups.signUp.class, ValidationGroups.accountCheck.class}, message = "비밀번호의 해쉬값은 64글자입니다")
    @NotNull(groups = {ValidationGroups.logIn.class, ValidationGroups.signUp.class, ValidationGroups.findPassword.class} , message = "패스워드는 비워둘 수 없습니다.")
    private String password;

    @NotNull(groups = {ValidationGroups.signUp.class ,ValidationGroups.findPassword.class }, message = "인증번호는 비워질 수 없습니다")
    @Size(min=6 , max= 6, groups = {ValidationGroups.signUp.class, ValidationGroups.findPassword.class}, message = "인증번호는 6글자입니다")
    @Pattern(regexp = "^[0-9]{6}$", message = "인증번호는 숫자만 가능합니다")
    private String secret;
    
    
    @NotNull(groups = {ValidationGroups.signUp.class, ValidationGroups.nicknameCheck.class}, message = "닉네임은 비워둘 수 없습니다.")
    @Size(min=1 , max= 20, groups = {ValidationGroups.signUp.class, ValidationGroups.nicknameCheck.class}, message = "닉네임은 1글자에서 20글자 사이입니다")
    @Pattern(regexp = "^[a-zA-Z가-힣0-9]{1,20}$", message = "닉네임의 특수문자와 초성은 사용불가능합니다")
    private String nickname;
    @NotNull( groups ={ ValidationGroups.findPassword.class, ValidationGroups.signUp.class } , message = "번호는 필수입니다.")
    @Size( min=11, max = 11,groups =  { ValidationGroups.findPassword.class, ValidationGroups.signUp.class}, message = "핸드폰번호는 11글자입니다.")
    @Pattern(regexp = "^[0-9]{11}$",groups ={ ValidationGroups.findPassword.class, ValidationGroups.signUp.class }, message = "핸드폰 번호는 숫자만 입력가능합니다")
    private String phone_number;

    @ApiModelProperty(hidden = true)
    private String salt;
    @ApiModelProperty(hidden = true)
    private String profile_image_url;
    @ApiModelProperty(hidden = true)
    private String role;
    @ApiModelProperty(hidden = true)
    private List<Authority> authority;
    @ApiModelProperty(hidden = true)
    private boolean is_deleted;
    @ApiModelProperty(hidden = true)
    private Timestamp created_at;
    @ApiModelProperty(hidden = true)
    private Timestamp updated_at;

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getProfile_image_url() {
        return profile_image_url;
    }

    public void setProfile_image_url(String profile_image_url) {
        this.profile_image_url = profile_image_url;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<Authority> getAuthority() {
        return authority;
    }

    public void setAuthority(List<Authority> authority) {
        this.authority = authority;
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

    public Timestamp getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Timestamp updated_at) {
        this.updated_at = updated_at;
    }
}
