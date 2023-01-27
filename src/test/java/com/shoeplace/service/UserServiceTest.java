package com.shoeplace.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.shoeplace.dto.UserSignUpDto;
import com.shoeplace.entity.User;
import com.shoeplace.exception.UserBusinessException;
import com.shoeplace.exception.UserErrorCode;
import com.shoeplace.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

	@InjectMocks
	UserService userService;

	@Mock
	UserRepository userRepository;

	@Test
	void createUserSuccess() {
		//given
		User user = User.builder()
			.loginId("test@test.com")
			.nickname("nick")
			.password("1234")
			.phoneNumber("01012341234")
			.build();

		given(userRepository.save(any()))
			.willReturn(user);

		UserSignUpDto.Request dto = UserSignUpDto.Request.builder()
			.loginId("test@test.com")
			.nickname("nick")
			.password("1234")
			.phoneNumber("01012341234")
			.build();

		//when
		String loginId = userService.createUser(dto);

		//then
		assertEquals(dto.getLoginId(), loginId);
	}

	@Test
	@DisplayName("DB에 존재하는 User 를 loginId로 조회하면, UserBusinessException 이 발생한다.")
	void createUserFail() {
		//given
		User user = User.builder()
			.loginId("test@test.com")
			.nickname("nick")
			.password("1234")
			.phoneNumber("01012341234")
			.build();

		given(userRepository.findByLoginId(any()))
			.willReturn(Optional.of(user));

		UserSignUpDto.Request dto = UserSignUpDto.Request.builder()
			.loginId("test@test.com")
			.nickname("nick")
			.password("1234")
			.phoneNumber("01012341234")
			.build();

		//when
		UserBusinessException exception = assertThrows(UserBusinessException.class,
			() -> userService.createUser(dto));

		//then
		assertEquals(exception.getMessage(), UserErrorCode.DUPLICATED_LOGIN_ID.getMessage());
	}
}