package com.shoeplace.controller;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.shoeplace.dto.UserSignUpDto;
import com.shoeplace.service.UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class UserController {

	private final UserService userService;

	@PostMapping("/user")
	public ResponseEntity<Map<String, String>> signUpUser(
		@RequestBody @Valid UserSignUpDto.Request request, BindingResult bindingResult) {

		Map<String, String> response = new HashMap<>();

		if (bindingResult.hasErrors()) {
			for (FieldError e : bindingResult.getFieldErrors()) {
				response.put(e.getField(), e.getDefaultMessage());
			}
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}

		String loginId = userService.createUser(request);
		response.put("loginId", loginId);

		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
}
