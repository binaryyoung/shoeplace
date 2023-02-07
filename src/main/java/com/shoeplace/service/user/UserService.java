package com.shoeplace.service.user;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shoeplace.common.MailComponent;
import com.shoeplace.dto.UserInfoDTO;
import com.shoeplace.dto.UserSignUpDTO;
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

	private static final String MAIL_AUTH_SUBJECT = "shoeplace 메일 인증을 진행해주세요.";
	private static final String MAIL_AUTH_TEXT = "<p>shoeplace 사이트 가입을 축하드립니다.<p>"
		+ "<p>아래 링크를 클릭하셔서 가입을 완료 하세요.</p>"
		+ "<div><a target='_blank' href='%s/user/email-auth?id=%s'>링크</a></div>;";

	private final UserRepository userRepository;
	private final MailComponent mailComponent;
	private final RedisTemplate redisTemplate;
	private final PasswordEncoder passwordEncoder;

	@Value("${spring.main.url}")
	private String url;

	@Transactional
	public void createUser(UserSignUpDTO.Request request) {
		if (userRepository.findByLoginId(request.getLoginId()).isPresent()) {
			throw new UserBusinessException(UserErrorCode.DUPLICATED_LOGIN_ID);
		}

		userRepository.save(User.builder()
			.loginId(request.getLoginId())
			.nickname(request.getNickname())
			.password(passwordEncoder.encode(request.getPassword()))
			.emailAuthYn(false)
			.role(UserRole.ROLE_USER)
			.status(UserStatus.UNAUTHENTICATED)
			.build());

		String uuid = UUID.randomUUID().toString();

		ValueOperations valueOperations = redisTemplate.opsForValue();
		valueOperations.set(uuid, request.getLoginId());
		redisTemplate.expire(uuid, 1, TimeUnit.DAYS);

		mailComponent.sendMail(request.getLoginId(), MAIL_AUTH_SUBJECT, String.format(MAIL_AUTH_TEXT, url, uuid));
	}

	@Transactional
	public void authEmail(String id) {
		ValueOperations valueOperations = redisTemplate.opsForValue();

		String loginId = (String)Optional.ofNullable(valueOperations.getAndDelete(id))
			.orElseThrow(() -> new UserBusinessException(UserErrorCode.USER_AUTHENTICATION_TIMEOUT));

		User user = getUserById(loginId);

		user.approveEmailAuth();
	}

	@Transactional(readOnly = true)
	public UserInfoDTO.Response inquireUserInfo(String loginId) {
		User user = getUserById(loginId);
		return UserInfoDTO.Response.of(user);
	}

	private User getUserById(String loginId) {
		return userRepository.findByLoginId(loginId).orElseThrow(
			() -> new UserBusinessException(UserErrorCode.USER_NOT_FOUND)
		);
	}

	@Transactional
	public void changeNickName(String loginId, String newNickName) {
		User user = getUserById(loginId);
		user.changeNickName(newNickName);
	}

	@Transactional
	public void changePhoneNumber(String loginId, String newPhoneNumber) {
		User user = getUserById(loginId);
		user.changePhoneNumber(newPhoneNumber);
	}

	@Transactional
	public void changePassword(String loginId, String beforePassword, String newPassword) {
		User user = getUserById(loginId);

		if (!passwordEncoder.matches(beforePassword, user.getPassword())) {
			throw new UserBusinessException(UserErrorCode.PASSWORD_NOT_MATCH);
		}

		user.changePassword(passwordEncoder.encode(newPassword));
	}
}
