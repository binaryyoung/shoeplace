package com.shoeplace.security.jwt;

import org.springframework.http.HttpHeaders;

public enum TokenType {
	ACCESS_TOKEN(HttpHeaders.AUTHORIZATION), REFRESH_TOKEN("refresh-token");

	private String header;

	TokenType(String header) {
		this.header = header;
	}

	public String getHeader() {
		return header;
	}
}
