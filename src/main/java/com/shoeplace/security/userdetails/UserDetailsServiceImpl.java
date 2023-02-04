package com.shoeplace.security.userdetails;

import static com.shoeplace.entity.UserStatus.*;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.shoeplace.entity.User;
import com.shoeplace.exception.UserErrorCode;
import com.shoeplace.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByLoginId(username)
			.orElseThrow(() -> new UsernameNotFoundException(UserErrorCode.USER_NOT_FOUND.getMessage()));

		if(!user.isEmailAuthYn()) {
			throw new AuthenticationServiceException("이메일 인증이 처리되지 않았습니다.");
		}

		if (user.getStatus() == SUSPENDED) {
			throw new AuthenticationServiceException("정지된 계정입니다.");
		}

		if (user.getStatus() == WITHDRAW) {
			throw new AuthenticationServiceException("탈퇴한 계정입니다.");
		}

		return UserDetailsImpl.builder()
							.username(user.getLoginId())
							.password(user.getPassword())
							.role(user.getRole().name())
							.build();
	}
}
