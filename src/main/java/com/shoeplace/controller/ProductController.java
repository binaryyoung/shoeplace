package com.shoeplace.controller;

import java.util.Objects;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.shoeplace.dto.ProductCreateDto;
import com.shoeplace.service.ProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

	private final ProductService productService;

	// 상품 정보를 입력하면 등록하고 등록된 상품 아이디와 이름을 반환한다.
	@PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
	public ResponseEntity<?> createProduct(
		@RequestPart("productInfo") ProductCreateDto.Request request,
		@RequestPart("image") MultipartFile[] files) {

		for (MultipartFile file : files) {
			if (!Objects.requireNonNull(file.getContentType()).startsWith("image/png")) {
				throw new RuntimeException("only png!");
			}
		}

		return ResponseEntity.ok(productService.createProduct(request, files));
	}

}
