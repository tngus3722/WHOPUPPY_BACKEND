package com.whopuppy.exception;

import com.whopuppy.enums.ErrorMessage;

public class AccessTokenNullException extends  NonCriticalException {

    public AccessTokenNullException(String className, ErrorMessage errorMessage) {
        super(className, errorMessage);

    }

    public AccessTokenNullException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}