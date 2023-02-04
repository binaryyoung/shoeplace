package com.shoeplace.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserSignUpDTO {

	@Builder
	@Getter
	@AllArgsConstructor
	@NoArgsConstructor
	@EqualsAndHashCode
	public static class Request {
		@NotNull
		@Email
		private String loginId;

		@NotNull
		private String nickname;

		@NotNull
		private String password;

		@NotNull
		private String phoneNumber;
	}
}