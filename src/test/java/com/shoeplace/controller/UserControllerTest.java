package com.shoeplace.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shoeplace.dto.UserSignUpDTO;
import com.shoeplace.security.config.SecurityConfig;
import com.shoeplace.service.user.UserService;

@WebMvcTest(value = UserController.class)
@Import(SecurityConfig.class)
class UserControllerTest {

	@MockBean
	private UserService userService;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void signUpUserSuccess() throws Exception {
		//given
		String loginId = "loginId@id.com";
		UserSignUpDTO.Request request = UserSignUpDTO.Request.builder()
			.loginId(loginId)
			.nickname("nick")
			.password("1234")
			.phoneNumber("01012341234")
			.build();

		//when
		//then
		mockMvc.perform(MockMvcRequestBuilders.post("/user")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isCreated());
	}

	@Test
	@DisplayName("아이디가 이메일 형식이 아니면, 400 상태코드를 반환한다")
	void signUpUserFail() throws Exception {
		//given
		String loginId = "notEmail";
		UserSignUpDTO.Request request = UserSignUpDTO.Request.builder()
			.loginId(loginId)
			.nickname("nick")
			.password("1234")
			.phoneNumber("01012341234")
			.build();

		//when
		//then
		mockMvc.perform(MockMvcRequestBuilders.post("/user")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isBadRequest());
	}

	@Test
	void authEmailSuccess() throws Exception {
		//given
		String uuid = "uuid";

		//when
		//then
		mockMvc.perform(MockMvcRequestBuilders.get("/user/email-auth?id=" + uuid)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());
	}
}