package com.shoeplace.security.config;

import static org.springframework.http.HttpMethod.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.shoeplace.security.filter.JwtExceptionFilter;
import com.shoeplace.security.filter.LoginCheckFilter;
import com.shoeplace.security.filter.LoginFilter;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private final LoginFilter loginFilter;
	private final LoginCheckFilter loginCheckFilter;
	private final AuthenticationEntryPoint authenticationEntryPoint;
	private final AccessDeniedHandler accessDeniedHandler;
	private final JwtExceptionFilter jwtExceptionFilter;

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws
		Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.httpBasic().disable()
			.csrf().disable()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			.addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class)
			.addFilterAt(loginCheckFilter, BasicAuthenticationFilter.class)
			.addFilterBefore(jwtExceptionFilter, LoginFilter.class)
			.exceptionHandling()
			.authenticationEntryPoint(authenticationEntryPoint)
			.accessDeniedHandler(accessDeniedHandler)
			.and()
			.authorizeRequests()
			.mvcMatchers(POST, "/user").permitAll()
			.mvcMatchers(GET, "/user/email-auth").permitAll()
			.mvcMatchers(POST, "/login").permitAll()
			.mvcMatchers(GET, "/user").hasRole("USER")
			.mvcMatchers(PATCH, "/user/**").hasRole("USER")
			.anyRequest().authenticated();

		return http.build();
	}
}
