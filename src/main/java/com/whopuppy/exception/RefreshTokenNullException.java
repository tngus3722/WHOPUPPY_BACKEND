package com.whopuppy.exception;

import com.whopuppy.enums.ErrorMessage;

public class RefreshTokenNullException  extends  NonCriticalException {

    public RefreshTokenNullException(String className, ErrorMessage errorMessage) {
        super(className, errorMessage);

    }

    public RefreshTokenNullException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}