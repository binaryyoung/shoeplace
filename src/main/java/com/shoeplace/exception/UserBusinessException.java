package com.shoeplace.exception;

import lombok.Getter;

@Getter
public class UserBusinessException extends RuntimeException {
	private final UserErrorCode errorCode;

	public UserBusinessException(UserErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}
}
