package com.shoeplace.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(UserBusinessException.class)
	public ResponseEntity<ErrorDto> handleAccountException(UserBusinessException e) {
		UserErrorCode errorCode = e.getErrorCode();
		return ResponseEntity.status(errorCode.getHttpStatus()).body(new ErrorDto(e.getMessage()));
	}
}
