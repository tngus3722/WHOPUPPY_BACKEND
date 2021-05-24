package com.whopuppy.enums;

import com.amazonaws.services.xray.model.Http;
import org.springframework.http.HttpStatus;

public enum ErrorMessage {
	UNDEFINED_EXCEPTION(0,"정의되지 않은 에러입니다.",HttpStatus.INTERNAL_SERVER_ERROR),
	NULL_POINTER_EXCEPTION(1,"NULL 여부를 확인해주세요",HttpStatus.BAD_REQUEST),
	JSON_PARSE_EXCEPTION(2,"JSON Parse 과정에 문제가 있습니다. 데이터를 확인해주세요",HttpStatus.BAD_REQUEST),
	AOP_XSS_SETTER_NO_EXSISTS_EXCEPTION(3,"해당 필드에 SETTER가 존재하지 않습니다.",HttpStatus.BAD_REQUEST),
	AOP_XSS_FIELD_NO_EXSISTS_EXCEPTION(4,"해당 필드에 FIELD가 존재하지 않습니다.",HttpStatus.BAD_REQUEST),
	ACCESS_FORBIDDEN_AUTH_INVALID_EXCEPTION(5,"ACCESS TOKEN이 VALID하지 않습니다.",HttpStatus.UNAUTHORIZED),
	REFRESH_FORBIDDEN_AUTH_INVALID_EXCEPTION(6,"REFRESH TOKEN이 VALID하지 않습니다.",HttpStatus.UNAUTHORIZED),
	REQUEST_INVALID_EXCEPTION(7,"입력 값이 올바르지 않습니다.",HttpStatus.BAD_REQUEST),
	ACCESS_FORBIDDEN_AUTH_EXPIRE_EXCEPTION(8,"ACCESS TOKEN이 EXPIRE 되었습니다.",HttpStatus.UNAUTHORIZED),
	REFRESH_FORBIDDEN_AUTH_EXPIRE_EXCEPTION(9,"REFRESH TOKEN이 EXPIRE 되었습니다.",HttpStatus.UNAUTHORIZED),
	SMS_EXPIRED_AUTH_EXCEPTION(10, " 인증번호가 만료되었습니다.",HttpStatus.BAD_REQUEST),
	SMS_NONE_AUTH_EXCEPTION(11, "번호 인증을 해주세요.",HttpStatus.BAD_REQUEST),
	SMS_SECRET_INVALID_EXCEPTION(12, "핸드폰 인증 번호를 확인해주세요.",HttpStatus.BAD_REQUEST),
	SMS_ALREADY_AUTHED(13, "이미 인증된 번호입니다.",HttpStatus.BAD_REQUEST),
	NO_USER_EXCEPTION(14, "가입되지 않은 계정입니다.",HttpStatus.BAD_REQUEST),
	VALIDATION_FAIL_EXCEPTION(15, "입력값의 조건이 올바르지 않습니다", HttpStatus.BAD_REQUEST),
	FORBIDDEN_EXCEPTION(16, "작업을 수행할 수 있는 권한이 없습니다.", HttpStatus.UNAUTHORIZED),
	INVALID_ACCESS_EXCEPTION(17, "존재하지 않는 게시글이거나 잘못된 접근입니다.", HttpStatus.BAD_REQUEST),
	INVALID_USER_EXCEPTION(18, "회원 정보가 존재하지 않습니다", HttpStatus.BAD_REQUEST),
	INVALID_SEMESTER_DATE_EXCEPTION(19, "잘못된 수강 학기입니다.", HttpStatus.BAD_REQUEST),
	PROHIBITED_ATTEMPT(20, "수강 후기는 한 과목에 하나만 작성 가능합니다.", HttpStatus.BAD_REQUEST),
	ACCOUNT_ALREADY_SIGNED_UP(21, "이미 가입된 계정입니다.", HttpStatus.BAD_REQUEST),
	NICKNAME_DUPLICATE(22, "중복된 닉네임입니다.", HttpStatus.BAD_REQUEST),
	PASSWORD_FAIL(23, "비밀번호가 틀렸습니다", HttpStatus.BAD_REQUEST),
	ACCOUNT_FAIL(24, "아이디가 틀렸습니다", HttpStatus.BAD_REQUEST),
	SMS_DAY_REQUEST_COUNT_EXCCED(25, "하루에 SMS인증은 5회까지만 가능합니다.",HttpStatus.BAD_REQUEST),
	AUTHORITY_NOT_EXIST(26,"요청한 권한은 존재하지 않습니다",HttpStatus.BAD_REQUEST),
	ROOT_AUTHORITY_CAN_NOT_DELETE_EXCEPTION(27,"루트 계정의 권한은 접근할 수 없습니다.",HttpStatus.BAD_REQUEST ),
	USER_IS_NOT_MANAGER(28, "해당 유저는 관리자계정이 아닙니다", HttpStatus.BAD_REQUEST),
	MULTIPART_FILE_NULL(29, "이미지가 NULL 입니다.", HttpStatus.BAD_REQUEST),
	MULTIPART_FILE_NOT_IMAGE(30,"이미지가 아닌 다른 파일은 저장할 수 없습니다.", HttpStatus.BAD_REQUEST),
	TARGET_ARTICLE_NOT_EXIST(31,"게시글이 존재하지 않습니다.", HttpStatus.BAD_REQUEST),
	IMAGE_FORBIDDEN_EXCEPTION(32,"이미지의 URL이 중복됩니다.", HttpStatus.BAD_REQUEST),
	NOT_AUTHOR(33, "작성자가 아닙니다.", HttpStatus.BAD_REQUEST),
	TARGET_ARTICLE_COMMENT_NOT_EXIST(34,"댓글이 존재하지 않습니다", HttpStatus.BAD_REQUEST),
	REGION_INVALID(35,"지역의 입력값이 잘못되었습니다.", HttpStatus.BAD_REQUEST),
	TOKEN_NULL(36, "토큰의 값이 비어있습니다.", HttpStatus.FORBIDDEN);

	Integer code;
	String errorMessage;
	HttpStatus httpStatus;
	ErrorMessage(int code, String errorMessage, HttpStatus httpStatus) {
		this.code = code;
		this.errorMessage = errorMessage;
		this.httpStatus = httpStatus;
	}


	public Integer getCode() {
		return code;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public HttpStatus getHttpStatus() {return httpStatus;}

	
}
