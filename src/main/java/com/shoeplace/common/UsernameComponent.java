package com.shoeplace.common;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class UsernameComponent {
	public static String getLoginId() {
		return ((UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
	}
}
