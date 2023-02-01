package com.shoeplace.security.filter;

import static com.shoeplace.security.jwt.JwtProvider.*;
import static com.shoeplace.security.jwt.TokenType.*;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.shoeplace.security.jwt.JwtProvider;

@Component
public class LoginCheckFilter extends OncePerRequestFilter {

	private final UserDetailsService userDetailsService;
	private final JwtProvider jwtProvider;

	public LoginCheckFilter(UserDetailsService userDetailsService, JwtProvider jwtProvider) {
		this.userDetailsService = userDetailsService;
		this.jwtProvider = jwtProvider;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws
		IOException, ServletException, JWTVerificationException {

		String accessToken = request.getHeader(ACCESS_TOKEN.getHeader());
		checkBearerToken(accessToken);

		if (!jwtProvider.validate(accessToken.substring(TOKEN_PREFIX.length()))) {
			String refreshToken = request.getHeader(REFRESH_TOKEN.getHeader());
			checkBearerToken(refreshToken);

			String username = jwtProvider.verify(refreshToken);
			response.setHeader(ACCESS_TOKEN.getHeader(),
				TOKEN_PREFIX + jwtProvider.createToken(username, ACCESS_TOKEN));
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			response.setStatus(HttpStatus.CREATED.value());
			return;
		}

		String username = jwtProvider.verify(accessToken.substring(TOKEN_PREFIX.length()));
		UserDetails userDetails = userDetailsService.loadUserByUsername(username);
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, "",
			userDetails.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(token);

		chain.doFilter(request, response);
	}

	private void checkBearerToken(String accessToken) {
		if (ObjectUtils.isEmpty(accessToken) || !accessToken.startsWith(TOKEN_PREFIX)) {
			throw new JWTVerificationException("jwt 토큰이 없거나, 인증 방법이 틀립니다");
		}
	}

}
