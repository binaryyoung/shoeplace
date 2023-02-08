package com.shoeplace.dto;

import com.shoeplace.validation.PhoneNumber;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserPhoneNumberDto {

	@Getter
	@AllArgsConstructor
	@NoArgsConstructor
	public static class Request {
		@PhoneNumber
		private String phoneNumber;
	}
}