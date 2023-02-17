package com.shoeplace.dto;

import java.time.LocalDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.shoeplace.entity.Product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ProductCreateDto {

	@AllArgsConstructor
	@NoArgsConstructor
	@Getter
	public static class Request {
		@NotBlank
		private String korName;

		@NotBlank
		private String engName;

		@NotBlank
		private String modelNumber;

		@NotNull
		private Double minSize;

		@NotNull
		private Double maxSize;

		@NotNull
		private Double unitSize;

		@NotNull
		private Long launchPrice;

		@NotNull
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private LocalDate launchDate;

		@NotNull
		private Long brandId;
	}

	@Getter
	@Builder
	public static class Response {
		private Long productId;
		private String korName;
		private String engName;

		public static Response of(Product product) {
			return Response.builder()
				.productId(product.getProductId())
				.korName(product.getKorName())
				.engName(product.getEngName())
				.build();
		}
	}
}
