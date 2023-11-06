package com.ssafy.dog.util;

import org.springframework.security.core.context.SecurityContextHolder;

import com.ssafy.dog.security.SecurityUserDto;

/*
 *  Security Context의 인증 객체로부터 다양한 정보를 뽑아서 제공하는 클래스
 * */
public abstract class SecurityUtils {

	public static Long getUserId() {
		return ((SecurityUserDto)(SecurityContextHolder.getContext().getAuthentication().getPrincipal())).getUserId();
	}

	public static String getUserLoginId() {
		return ((SecurityUserDto)(SecurityContextHolder.getContext()
			.getAuthentication()
			.getPrincipal())).getUserLoginId();
	}

	public static SecurityUserDto getUser() {
		return (SecurityUserDto)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
}

