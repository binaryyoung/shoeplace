package com.shoeplace.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shoeplace.dto.UserInfoDTO;
import com.shoeplace.dto.UserSignUpDTO;
import com.shoeplace.service.user.UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController("/user")
public class UserController {

	private final UserService userService;

	@PostMapping
	public ResponseEntity<?> signUpUser(
		@RequestBody @Valid UserSignUpDTO.Request request) {
		userService.createUser(request);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@GetMapping("email-auth")
	public ResponseEntity<?> authEmail(@RequestParam String id) {
		userService.authEmail(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@GetMapping
	public ResponseEntity<UserInfoDTO.Response> inquireMemberInfo() {
		String loginId = (String)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return ResponseEntity.ok(userService.inquireUserInfo(loginId));
	}
}
