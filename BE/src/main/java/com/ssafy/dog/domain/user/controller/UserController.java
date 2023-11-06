package com.ssafy.dog.domain.user.controller;

import javax.validation.Valid;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.dog.common.api.Api;
import com.ssafy.dog.domain.user.dto.request.UserLoginReq;
import com.ssafy.dog.domain.user.dto.request.UserSignupReq;
import com.ssafy.dog.domain.user.repository.UserRepository;
import com.ssafy.dog.domain.user.service.UserService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/api/user")
@RequiredArgsConstructor
@RestController
public class UserController {
	private final PasswordEncoder passwordEncoder;
	private final UserService userService;
	private final UserRepository userRepository;

	@PostMapping("/signup")
	public Api<?> signUp(@Valid @RequestBody UserSignupReq userSignupReq) {
		return userService.create(userSignupReq);
	}

	@PostMapping("/login")
	public Api<?> login(@Valid @RequestBody UserLoginReq userLoginReq) {

		return userService.login(userLoginReq);
	}
}
