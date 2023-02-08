package com.shoeplace.controller;

import static com.shoeplace.common.UsernameComponent.*;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shoeplace.dto.AddressCreateDto;
import com.shoeplace.service.address.AddressService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/address")
public class AddressController {

	private final AddressService addressService;

	@PostMapping
	public ResponseEntity<?> createAddress(@RequestBody @Valid AddressCreateDto.Request request) {
		addressService.createService(request, getLoginId());
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
}
