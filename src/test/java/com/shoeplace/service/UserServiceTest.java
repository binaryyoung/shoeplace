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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.shoeplace.common.MailComponent;
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

	@Mock
	MailComponent mailComponent;

	@Mock
	RedisTemplate redisTemplate;

	@Mock
	ValueOperations valueOperations;

	@Mock
	PasswordEncoder passwordEncoder;

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

		given(redisTemplate.opsForValue()).willReturn(valueOperations);

		//when //then
		userService.createUser(dto);
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
		assertEquals(UserErrorCode.DUPLICATED_LOGIN_ID, exception.getErrorCode());
	}

	@Test
	void authEmailSuccess() {
		//given
		String uuid = "uuid";
		String loginId = "test@test.com";

		User user = User.builder()
			.loginId(loginId)
			.nickname("nick")
			.password("1234")
			.phoneNumber("01012341234")
			.build();

		given(redisTemplate.opsForValue()).willReturn(valueOperations);
		given(valueOperations.getAndDelete(uuid)).willReturn(anyString());
		given(userRepository.findByLoginId(loginId)).willReturn(Optional.of(user));

		//when
		//then
		userService.authEmail(uuid);
	}

	@Test
	@DisplayName("인증 데이터가 만료된 경우, UserBusinessException 이 발생한다.")
	void authEmailFail() {
		//given
		String uuid = "NoUuid";
		String loginId = "test@test.com";

		given(redisTemplate.opsForValue()).willReturn(valueOperations);
		given(valueOperations.getAndDelete(uuid)).willReturn(null);

		//when
		UserBusinessException exception = assertThrows(UserBusinessException.class,
			() -> userService.authEmail(uuid));

		//then
		assertEquals(UserErrorCode.USER_AUTHENTICATION_TIMEOUT, exception.getErrorCode());
	}

	@Test
	@DisplayName("User의 이메일 인증 여부가 True면, UserBusinessException 이 발생한다.")
	void authEmailFail2() {
		//given
		String uuid = "NoUuid";
		String loginId = "test@test.com";

		User user = User.builder()
			.loginId(loginId)
			.nickname("nick")
			.password("1234")
			.phoneNumber("01012341234")
			.emailAuthYn(true)
			.build();

		given(redisTemplate.opsForValue()).willReturn(valueOperations);
		given(valueOperations.getAndDelete(uuid)).willReturn(loginId);
		given(userRepository.findByLoginId(loginId)).willReturn(Optional.of(user));

		//when
		UserBusinessException exception = assertThrows(UserBusinessException.class,
			() -> userService.authEmail(uuid));

		//then
		assertEquals(UserErrorCode.ALREADY_AUTHENTICATED_EMAIL_ACCOUNT, exception.getErrorCode());
	}
}