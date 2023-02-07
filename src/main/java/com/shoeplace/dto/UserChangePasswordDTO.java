package com.shoeplace.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserChangePasswordDTO {
	@Getter
	@AllArgsConstructor
	@NoArgsConstructor
	public static class Request {
		private String beforePassword;
		private String newPassword;
	}
}
