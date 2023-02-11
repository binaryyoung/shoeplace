package com.shoeplace.common;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.experimental.UtilityClass;

@UtilityClass
public class UsernameUtil {
	public static String getLoginId() {
		return ((UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
	}
}
