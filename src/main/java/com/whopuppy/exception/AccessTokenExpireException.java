package com.whopuppy.exception;


import com.whopuppy.enums.ErrorMessage;

public class AccessTokenExpireException extends NonCriticalException {

    public AccessTokenExpireException(String className, ErrorMessage errorMessage) {
        super(className, errorMessage);

    }

    public AccessTokenExpireException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
