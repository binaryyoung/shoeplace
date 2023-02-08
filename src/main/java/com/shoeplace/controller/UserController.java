package com.shoeplace.controller;

import static com.shoeplace.common.UsernameUtil.*;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shoeplace.dto.UserChangeNicknameDto;
import com.shoeplace.dto.UserChangePasswordDto;
import com.shoeplace.dto.UserInfoDto;
import com.shoeplace.dto.UserPhoneNumberDto;
import com.shoeplace.dto.UserSignUpDto;
import com.shoeplace.dto.UserWithdrawDto;
import com.shoeplace.service.user.UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

	private final UserService userService;

	@PostMapping
	public ResponseEntity<?> signUpUser(@RequestBody @Valid UserSignUpDto.Request request) {
		userService.createUser(request);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@GetMapping("/email-auth")
	public ResponseEntity<?> authEmail(@RequestParam String id) {
		userService.authEmail(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@GetMapping
	public ResponseEntity<UserInfoDto.Response> inquireMemberInfo() {
		return ResponseEntity.ok(userService.inquireUserInfo(getLoginId()));
	}

	@PatchMapping("/nickname")
	public ResponseEntity<?> changeNickName(@RequestBody @Valid UserChangeNicknameDto.Request request) {
		userService.changeNickName(getLoginId(), request.getNickname());
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PatchMapping("/phoneNumber")
	public ResponseEntity<?> changePhoneNumber(@RequestBody @Valid UserPhoneNumberDto.Request request) {
		userService.changePhoneNumber(getLoginId(), request.getPhoneNumber());
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PatchMapping("/password")
	public ResponseEntity<?> changePassword(@RequestBody @Valid UserChangePasswordDto.Request request) {
		userService.changePassword(getLoginId(), request.getBeforePassword(), request.getNewPassword());
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@DeleteMapping("/withdraw")
	public ResponseEntity<?> withdraw(@RequestBody @Valid UserWithdrawDto.Request request) {
		userService.withdraw(getLoginId(), request.getPassword());
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
