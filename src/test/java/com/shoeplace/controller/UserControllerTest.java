package com.shoeplace.controller;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shoeplace.dto.UserSignUpDto;
import com.shoeplace.service.UserService;

@WebMvcTest(UserController.class)
class UserControllerTest {

	@MockBean
	private UserService userService;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void signUpUser() throws Exception {
		//given
		String loginId = "loginId@id.com";
		UserSignUpDto.Request request = UserSignUpDto.Request.builder()
			.loginId(loginId)
			.nickname("nick")
			.password("1234")
			.phoneNumber("01012341234")
			.build();

		given(userService.createUser(request)).willReturn(loginId);

		//when
		//then
		mockMvc.perform(MockMvcRequestBuilders.post("/user")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.loginId").value(loginId));
	}
}