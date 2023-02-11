package com.shoeplace.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AddressDeleteDto {
	@Getter
	@AllArgsConstructor
	@NoArgsConstructor
	public static class Request {
		private long id;
	}
}
