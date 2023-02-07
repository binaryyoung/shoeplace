package com.shoeplace.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shoeplace.dto.UserChangeNicknameDTO;
import com.shoeplace.dto.UserChangePasswordDTO;
import com.shoeplace.dto.UserInfoDTO;
import com.shoeplace.dto.UserPhoneNumberDTO;
import com.shoeplace.dto.UserSignUpDTO;
import com.shoeplace.service.user.UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

	private final UserService userService;

	@PostMapping
	public ResponseEntity<?> signUpUser(@RequestBody @Valid UserSignUpDTO.Request request) {
		userService.createUser(request);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@GetMapping("/email-auth")
	public ResponseEntity<?> authEmail(@RequestParam String id) {
		userService.authEmail(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@GetMapping
	public ResponseEntity<UserInfoDTO.Response> inquireMemberInfo() {
		return ResponseEntity.ok(userService.inquireUserInfo(getLoginId()));
	}

	@PatchMapping("/nickname")
	public ResponseEntity<?> changeNickName(@RequestBody @Valid UserChangeNicknameDTO.Request request) {
		userService.changeNickName(getLoginId(), request.getNickname());
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PatchMapping("/phoneNumber")
	public ResponseEntity<?> changePhoneNumber(@RequestBody @Valid UserPhoneNumberDTO.Request request) {
		userService.changePhoneNumber(getLoginId(), request.getPhoneNumber());
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PatchMapping("/password")
	public ResponseEntity<?> changePassword(@RequestBody @Valid UserChangePasswordDTO.Request request) {
		userService.changePassword(getLoginId(), request.getBeforePassword(), request.getNewPassword());
		return new ResponseEntity<>(HttpStatus.OK);
	}

	private String getLoginId() {
		return ((UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
	}
}
