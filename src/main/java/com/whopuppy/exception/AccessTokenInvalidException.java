package com.whopuppy.exception;


import com.whopuppy.enums.ErrorMessage;

public class AccessTokenInvalidException extends CriticalException {

    public AccessTokenInvalidException(String className, ErrorMessage errorMessage) {
        super(className, errorMessage);

    }

    public AccessTokenInvalidException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
