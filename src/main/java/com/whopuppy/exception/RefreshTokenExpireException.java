package com.whopuppy.exception;


import com.whopuppy.enums.ErrorMessage;

public class RefreshTokenExpireException extends NonCriticalException {
    public RefreshTokenExpireException(String className, ErrorMessage errorMessage) {
        super(className, errorMessage);

    }

    public RefreshTokenExpireException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
