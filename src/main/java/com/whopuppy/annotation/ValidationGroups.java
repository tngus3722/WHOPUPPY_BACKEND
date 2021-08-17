package com.whopuppy.annotation;

import javax.validation.groups.Default;

/**
 * Utility classes to distinct CRUD validations.<br>
 * <br>
 * Used with the
 * {@link org.springframework.validation.annotation.Validated @Validated}
 * Spring annotation.
 */
public final class ValidationGroups {

    private ValidationGroups() { }
    public interface Create extends  Default{};
    public interface Read extends  Default{};
    public interface Update extends  Default{};
    public interface Delete extends  Default{};

    public interface signUp extends  Create{};
    public interface logIn extends Read{};
    public interface sendSms extends Create{};
    public interface configSms extends Update{};
    public interface findPassword extends Update{};
    public interface nicknameCheck extends  Read{};
    public interface accountCheck extends  Read{};
    public interface authority extends Create{};
    public interface postCommunity extends Create{};
    public interface postComment extends Create{};
    public interface animalComment extends Default{};
    public interface animalList extends Default{};
    public interface Send extends Create{};

}