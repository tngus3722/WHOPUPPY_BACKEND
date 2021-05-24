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

    public interface signUp extends  Default{};
    public interface logIn extends Default{};
    public interface sendSms extends Default{};
    public interface configSms extends Default{};
    public interface findPassword extends Default{};
    public interface nicknameCheck extends  Default{};
    public interface accountCheck extends  Default{};
    public interface authority extends Default{};
    public interface postCommunity extends Default{};
    public interface postComment extends Default{};
    public interface animalComment extends Default{};
    public interface animalList extends Default{};
}