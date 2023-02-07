package com.shoeplace.dto;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserWithdrawDTO {
	@Getter
	@AllArgsConstructor
	@NoArgsConstructor
	public static class Request {
		@NotBlank
		private String password;
	}
}
