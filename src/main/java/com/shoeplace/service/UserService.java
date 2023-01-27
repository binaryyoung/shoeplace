package com.shoeplace.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shoeplace.dto.UserSignUpDto;
import com.shoeplace.entity.User;
import com.shoeplace.entity.UserRole;
import com.shoeplace.entity.UserStatus;
import com.shoeplace.exception.UserBusinessException;
import com.shoeplace.exception.UserErrorCode;
import com.shoeplace.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {

	private final UserRepository userRepository;

	@Transactional
	public String createUser(UserSignUpDto.Request request) {
		if (userRepository.findByLoginId(request.getLoginId()).isPresent()) {
			throw new UserBusinessException(UserErrorCode.DUPLICATED_LOGIN_ID);
		}

		userRepository.save(User.builder()
			.loginId(request.getLoginId())
			.nickname(request.getNickname())
			.password(request.getPassword())
			.emailAuthYn(false)
			.role(UserRole.USER)
			.status(UserStatus.UNAUTHENTICATED)
			.build());

		return request.getLoginId();
	}
}