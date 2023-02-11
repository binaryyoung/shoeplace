package com.shoeplace.dto;

import com.shoeplace.entity.Address;

import lombok.Builder;
import lombok.Getter;

public class AddressInquireDto {

	@Getter
	@Builder
	public static class Response {
		private Long id;
		private String nickname;
		private String address1;
		private String address2;
		private String receiverName;
		private String phoneNumber;

		public static Response of(Address address) {
			return Response.builder()
				.id(address.getAddressId())
				.nickname(address.getNickname())
				.address1(address.getAddress1())
				.address2(address.getAddress2())
				.receiverName(address.getReceiverName())
				.phoneNumber(address.getPhoneNumber())
				.build();
		}
	}
}
