package com.whopuppy.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


public class BaseResponse {
    private String message;
    private HttpStatus httpStatus;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public BaseResponse(String message, HttpStatus httpStatus){
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
