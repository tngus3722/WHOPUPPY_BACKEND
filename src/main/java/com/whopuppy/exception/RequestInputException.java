package com.whopuppy.exception;


import com.whopuppy.enums.ErrorMessage;

public class RequestInputException extends NonCriticalException {

    public RequestInputException(String className, ErrorMessage errorMessage) {
        super(className, errorMessage);

    }
    public RequestInputException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
