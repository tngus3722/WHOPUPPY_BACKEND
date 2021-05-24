package com.whopuppy.exception;

import com.whopuppy.enums.ErrorMessage;


public class CriticalException extends BaseException {

	public CriticalException(String className, ErrorMessage errorMessage) {
		super(className, errorMessage);
	}
	public CriticalException(ErrorMessage errorMessage) {
		super(errorMessage);
	}
}
