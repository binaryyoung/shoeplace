package com.shoeplace.dto;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserSignInDTO {

	@Getter
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor
	public static class Request {
		@NotBlank
		private String username;
		@NotBlank
		private String password;
	}
}
