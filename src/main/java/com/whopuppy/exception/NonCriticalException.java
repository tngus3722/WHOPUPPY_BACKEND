package com.whopuppy.exception;

import com.whopuppy.enums.ErrorMessage;


public class NonCriticalException extends BaseException {

	public NonCriticalException(String className, ErrorMessage errorMessage) {
		super(className, errorMessage);
	}
	public NonCriticalException(ErrorMessage errorMessage) {
		super(errorMessage);
	}
}
