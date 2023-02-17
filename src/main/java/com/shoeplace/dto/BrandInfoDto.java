package com.shoeplace.dto;

import javax.validation.constraints.NotNull;

import com.shoeplace.entity.Brand;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class BrandInfoDto {
	@Getter
	@AllArgsConstructor
	@NoArgsConstructor
	public static class Request {
		@NotNull
		private String name;

		@NotNull
		private Integer page;
	}

	@Builder
	@Getter
	public static class Response {
		private Long brandId;
		private String korName;
		private String engName;

		public static Response of(Brand brand) {
			return Response.builder()
				.brandId(brand.getBrandId())
				.korName(brand.getKorName())
				.engName(brand.getEngName())
				.build();
		}
	}
}
