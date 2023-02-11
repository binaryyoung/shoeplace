package com.shoeplace.dto;

import javax.validation.constraints.NotBlank;

import com.shoeplace.validation.PhoneNumber;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AddressUpdateDto {

	@Getter
	@AllArgsConstructor
	@NoArgsConstructor
	public static class Request {
		private long id;

		@NotBlank
		private String address1;

		@NotBlank
		private String address2;

		@NotBlank
		private String receiverName;

		@PhoneNumber
		private String phoneNumber;
	}
}
