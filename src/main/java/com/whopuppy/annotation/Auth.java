package com.whopuppy.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Auth {

    // 루트, 관리자, 일반유저
    enum Role { ROOT, MANAGER, NORMAL }

    // TODO 일반 유저 권한 = NONE
    enum Authority { WANT_DO_ADOPT, WANT_TAKE_ADOPT, ADOPT_REVIEW, SNACK, NONE }

    Role role() default Role.NORMAL;
    Authority authority() default Authority.NONE;
}
