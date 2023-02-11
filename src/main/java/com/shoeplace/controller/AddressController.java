package com.shoeplace.controller;

import static com.shoeplace.common.UsernameUtil.*;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shoeplace.dto.AddressCreateDto;
import com.shoeplace.dto.AddressDeleteDto;
import com.shoeplace.dto.AddressInquireDto;
import com.shoeplace.dto.AddressUpdateDto;
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

	@GetMapping
	public ResponseEntity<List<AddressInquireDto.Response>> inquireAddress() {
		return new ResponseEntity<>(addressService.inquireAddress(getLoginId()), HttpStatus.OK);
	}

	@PatchMapping
	public ResponseEntity<?> updateAddress(@RequestBody @Valid AddressUpdateDto.Request request) {
		addressService.updateAddress(request, getLoginId());
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@DeleteMapping
	public ResponseEntity<?> deleteAddress(@RequestBody @Valid AddressDeleteDto.Request request) {
		addressService.deleteAddress(request.getId());
		return new ResponseEntity<>(HttpStatus.OK);
	}
}