package com.ssafy.dog.domain.user.controller;

import javax.validation.Valid;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.dog.common.api.Api;
import com.ssafy.dog.domain.user.dto.UserDto;
import com.ssafy.dog.domain.user.dto.UserLoginDto;
import com.ssafy.dog.domain.user.dto.UserSignupDto;
import com.ssafy.dog.domain.user.repository.UserRepository;
import com.ssafy.dog.domain.user.service.UserService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/user")
@RequiredArgsConstructor
@RestController
public class UserController {
	private final PasswordEncoder passwordEncoder;
	private final UserService userService;
	private final UserRepository userRepository;

	@PostMapping("/signup")
	public Api<?> signUp(@Valid @RequestBody UserSignupDto userSignupDto) {
		UserDto userDto = new UserDto();
		userDto.setUserLoginId(userSignupDto.getUserLoginId());
		userDto.setUserPhone(userSignupDto.getUserPhone());
		userDto.setUserPw(userSignupDto.getUserPw1());
		userDto.setUserNickname(userSignupDto.getUserNickname());
		userDto.setUserTermsAgreed(userSignupDto.getUserTermsAgreed());

		return userService.create(userDto);
	}

	@PostMapping("/login")
	public Api<?> login(@Valid @RequestBody UserLoginDto userLoginDto) {

		return userService.login(userLoginDto);
	}
}
