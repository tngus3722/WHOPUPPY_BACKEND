package com.whopuppy.exception;


import com.whopuppy.enums.ErrorMessage;

public class RefreshTokenInvalidException extends CriticalException {
    public RefreshTokenInvalidException(String className, ErrorMessage errorMessage) {
        super(className, errorMessage);

    }

    public RefreshTokenInvalidException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
