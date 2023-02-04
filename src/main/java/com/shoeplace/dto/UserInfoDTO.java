package com.shoeplace.dto;

import com.shoeplace.entity.User;

import lombok.Builder;
import lombok.Getter;

public class UserInfoDTO {

	@Getter
	@Builder
	public static class Response {
		private String loginId;
		private String nickname;
		private String phoneNumber;

		public static UserInfoDTO.Response of(User user) {
			return Response.builder()
				.loginId(user.getLoginId())
				.nickname(user.getNickname())
				.phoneNumber(user.getPhoneNumber())
				.build();
		}
	}
}
