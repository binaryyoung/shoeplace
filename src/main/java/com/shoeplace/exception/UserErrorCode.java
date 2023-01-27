package com.shoeplace.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum UserErrorCode {

	DUPLICATED_LOGIN_ID(HttpStatus.CONFLICT, "이미 존재하는 이메일 계정입니다");

	private HttpStatus httpStatus;
	private String message;

	UserErrorCode(HttpStatus httpStatus, String message) {
		this.httpStatus = httpStatus;
		this.message = message;
	}
}