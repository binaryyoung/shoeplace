package com.shoeplace.security.filter;

import static com.shoeplace.security.jwt.JwtProvider.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shoeplace.dto.UserSignInDTO;
import com.shoeplace.exception.ErrorDto;
import com.shoeplace.security.jwt.JwtProvider;
import com.shoeplace.security.jwt.TokenType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class LoginFilter extends AbstractAuthenticationProcessingFilter {

	private final ObjectMapper objectMapper;
	private final JwtProvider jwtProvider;

	public LoginFilter(@Lazy AuthenticationManager authenticationManager,
		ObjectMapper objectMapper, JwtProvider jwtProvider, JwtProvider jwtProvider1) {
		super("/login", authenticationManager);
		this.objectMapper = objectMapper;
		this.jwtProvider = jwtProvider1;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws
		AuthenticationException {
		try {
			UserSignInDTO.Request signInDto = objectMapper.readValue(request.getInputStream(),
				UserSignInDTO.Request.class);

			UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(signInDto.getUsername(),
				signInDto.getPassword());

			return this.getAuthenticationManager().authenticate(token);

		} catch (IOException e) {
			throw new AuthenticationServiceException("request 메시지 형식이 맞지 않습니다.");
		} catch (BadCredentialsException e) {
			throw new BadCredentialsException("패스워드가 일치하지 않습니다.");
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
		Authentication authResult) throws IOException, ServletException {
		UserDetails userDetails = (UserDetails)authResult.getPrincipal();
		response.setHeader(TokenType.ACCESS_TOKEN.getHeader(),
			TOKEN_PREFIX + jwtProvider.createToken(userDetails.getUsername(), TokenType.ACCESS_TOKEN));
		response.setHeader(TokenType.REFRESH_TOKEN.getHeader(),
			TOKEN_PREFIX + jwtProvider.createToken(userDetails.getUsername(), TokenType.REFRESH_TOKEN));
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setStatus(HttpStatus.CREATED.value());
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException failed) throws IOException, ServletException {
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.getWriter().write(objectMapper.writeValueAsString(new ErrorDto(failed.getMessage())));
	}
}
