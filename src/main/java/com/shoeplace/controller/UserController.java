package com.shoeplace.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shoeplace.dto.UserSignUpDto;
import com.shoeplace.service.UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class UserController {

	private final UserService userService;

	@PostMapping("/user")
	public ResponseEntity<UserSignUpDto.Response> signUpUser(
		@RequestBody @Valid UserSignUpDto.Request request) {
		return new ResponseEntity<>(userService.createUser(request), HttpStatus.CREATED);
	}

	@GetMapping("/user/email-auth")
	public ResponseEntity<?> authEmail(@RequestParam String id) {
		userService.authEmail(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
