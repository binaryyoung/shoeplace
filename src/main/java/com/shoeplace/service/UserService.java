package com.shoeplace.service;

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

	private final String mailAuthSubjectStr = "shoeplace 메일 인증을 진행해주세요.";

	@Value("${spring.main.url}")
	private String url;

	private static final String mailAuthTextStr1 = "<p>shoeplace 사이트 가입을 축하드립니다.<p><p>아래 링크를 클릭하셔서 가입을 완료 하세요.</p>"
		+ "<div><a target='_blank' href='";
	private static final String mailAuthTextStr2 = "/user/email-auth?id=";
	private static final String mailAuthTextStr3 = "'> 가입 완료 </a></div>;";

	private final UserRepository userRepository;
	private final MailComponent mailComponent;
	private final RedisTemplate redisTemplate;
	private final PasswordEncoder passwordEncoder;

	@Transactional
	public UserSignUpDto.Response createUser(UserSignUpDto.Request request) {
		if (userRepository.findByLoginId(request.getLoginId()).isPresent()) {
			throw new UserBusinessException(UserErrorCode.DUPLICATED_LOGIN_ID);
		}

		userRepository.save(User.builder()
			.loginId(request.getLoginId())
			.nickname(request.getNickname())
			.password(passwordEncoder.encode(request.getPassword()))
			.emailAuthYn(false)
			.role(UserRole.USER)
			.status(UserStatus.UNAUTHENTICATED)
			.build());

		String uuid = UUID.randomUUID().toString();

		ValueOperations valueOperations = redisTemplate.opsForValue();
		valueOperations.set(uuid, request.getLoginId());
		redisTemplate.expire(uuid, 1, TimeUnit.DAYS);

		mailComponent.sendMail(request.getLoginId(), mailAuthSubjectStr,
			mailAuthTextStr1 + url + mailAuthTextStr2 + uuid + mailAuthTextStr3);

		return new UserSignUpDto.Response(request.getLoginId());
	}

	@Transactional
	public void authEmail(String id) {
		ValueOperations valueOperations = redisTemplate.opsForValue();

		String loginId = (String)Optional.ofNullable(valueOperations.getAndDelete(id))
			.orElseThrow(() -> new UserBusinessException(UserErrorCode.USER_AUTHENTICATION_TIMEOUT));

		User user = userRepository.findByLoginId(loginId).orElseThrow(
			() -> new UserBusinessException(UserErrorCode.USER_NOT_FOUND)
		);

		user.approveEmailAuth();
	}
}
