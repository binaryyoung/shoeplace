package com.shoeplace.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import com.shoeplace.validation.PhoneNumber;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserSignUpDto {

	@Builder
	@Getter
	@AllArgsConstructor
	@NoArgsConstructor
	@EqualsAndHashCode
	public static class Request {
		@Email
		private String loginId;

		@NotBlank
		private String nickname;

		@NotBlank
		private String password;

		@PhoneNumber
		private String phoneNumber;
	}
}