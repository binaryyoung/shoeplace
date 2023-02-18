package com.shoeplace.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ErrorCode {

	// User error
	DUPLICATED_LOGIN_ID(HttpStatus.CONFLICT, "이미 존재하는 이메일 계정입니다."),
	USER_AUTHENTICATION_TIMEOUT(HttpStatus.REQUEST_TIMEOUT, "인증 시간이 만료되었습니다."),
	USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 회원입니다."),
	ALREADY_AUTHENTICATED_EMAIL_ACCOUNT(HttpStatus.CONFLICT, "이미 인증된 이메일 계정입니다."),
	PASSWORD_NOT_MATCH(HttpStatus.CONFLICT, "패스워드가 일치하지 않습니다."),

	// Address error
	ADDRESS_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, "주소는 최대 10개까지 저장 가능합니다."),
	DUPLICATED_ADDRESS_NICKNAME(HttpStatus.CONFLICT, "주소 별칭은 중복될 수 없습니다."),
	ADDRESS_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 주소입니다."),
	BRAND_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 브랜드입니다."),
	FILE_UPLOAD_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패했습니다");

	private HttpStatus httpStatus;
	private String message;

	ErrorCode(HttpStatus httpStatus, String message) {
		this.httpStatus = httpStatus;
		this.message = message;
	}
}
