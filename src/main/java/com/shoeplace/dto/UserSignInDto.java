package com.shoeplace.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserSignInDto {

	@Getter
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor
	public static class Request {
		private String username;
		private String password;
	}
}
