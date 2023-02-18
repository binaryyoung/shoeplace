package com.shoeplace.controller;

import javax.validation.Valid;

import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shoeplace.dto.BrandInfoDto;
import com.shoeplace.service.brand.BrandService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/brand")
public class BrandController {

	private final BrandService brandService;

	@GetMapping
	public ResponseEntity<Slice<BrandInfoDto.Response>> inquireBrandInfo(
		@RequestBody @Valid BrandInfoDto.Request request) {
		Slice<BrandInfoDto.Response> response = brandService.inquireBrandInfo(request.getName(), request.getPage());
		return ResponseEntity.ok(response);
	}
}
