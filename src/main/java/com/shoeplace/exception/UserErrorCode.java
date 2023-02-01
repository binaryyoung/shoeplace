package com.shoeplace.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum UserErrorCode {

	DUPLICATED_LOGIN_ID(HttpStatus.CONFLICT, "이미 존재하는 이메일 계정입니다."),
	USER_AUTHENTICATION_TIMEOUT(HttpStatus.REQUEST_TIMEOUT, "인증 시간이 만료되었습니다."),
	USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 회원입니다."),
	ALREADY_AUTHENTICATED_EMAIL_ACCOUNT(HttpStatus.CONFLICT, "이미 인증된 이메일 계정입니다.");

	private HttpStatus httpStatus;
	private String message;

	UserErrorCode(HttpStatus httpStatus, String message) {
		this.httpStatus = httpStatus;
		this.message = message;
	}
}
