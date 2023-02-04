package com.shoeplace.security.jwt;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtProvider {

	public static final String TOKEN_PREFIX = "Bearer "; // jwt를 쓰는 경우 "Bearer " 를 앞에 붙여서 보내는 규약이 있음.

	// private static final long ACCESS_TIME_SECOND = 60 * 30;
	private static final long ACCESS_TIME_SECOND = 10;
	private static final long REFRESH_TIME_SECOND = 60 * 30;

	@Value("{shoeplace.jwt.secret}")
	private String key;

	public String createToken(String username, TokenType tokenType) {
		return JWT.create()
			.withSubject(username)
			.withClaim("exp", getExpireTimeByTokenType(tokenType))
			.sign(Algorithm.HMAC512(key));
	}

	private static long getExpireTimeByTokenType(TokenType tokenType) {
		long expiredTime = Instant.now().getEpochSecond();
		switch (tokenType) {
			case ACCESS_TOKEN:
				expiredTime += ACCESS_TIME_SECOND;
				break;
			case REFRESH_TOKEN:
				expiredTime += REFRESH_TIME_SECOND;
				break;
			default:
				throw new IllegalStateException("Unexpected value: " + tokenType);
		}
		return expiredTime;
	}

	public boolean validate(String token) throws JWTVerificationException {
		try {
			JWT.require(Algorithm.HMAC512(key)).build().verify(token).getSubject();
			return true;
		} catch (SignatureVerificationException e) {
			log.info("잘못된 JWT 서명입니다.");
		} catch (TokenExpiredException e) {
			log.info("만료된 JWT 토큰입니다.");
		}
		return false;
	}

	public String verify(String token) throws JWTVerificationException {
		try {
			return JWT.require(Algorithm.HMAC512(key)).build().verify(token).getSubject();
		} catch (SignatureVerificationException e) {
			throw new JWTVerificationException("잘못된 JWT 서명입니다.");
		} catch (TokenExpiredException e) {
			throw new JWTVerificationException("만료된 JWT 토큰입니다.");
		}
	}
}
