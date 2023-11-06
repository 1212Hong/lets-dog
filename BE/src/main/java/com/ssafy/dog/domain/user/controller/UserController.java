package com.ssafy.dog.domain.user.controller;

import javax.validation.Valid;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.dog.common.api.Api;
import com.ssafy.dog.domain.user.dto.request.UserLoginReq;
import com.ssafy.dog.domain.user.dto.request.UserSignupReq;
import com.ssafy.dog.domain.user.dto.request.UserUpdateReq;
import com.ssafy.dog.domain.user.repository.UserRepository;
import com.ssafy.dog.domain.user.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RequestMapping("/api/user")
@RequiredArgsConstructor
@RestController
public class UserController {
	private final PasswordEncoder passwordEncoder;
	private final UserService userService;
	private final UserRepository userRepository;

	@PostMapping("/signup")
	@Operation(summary = "회원가입")
	public Api<?> signUp(@Valid @RequestBody UserSignupReq userSignupReq) {
		return userService.create(userSignupReq);
	}

	@PostMapping("/login")
	@Operation(summary = "로그인")
	public Api<?> login(@Valid @RequestBody UserLoginReq userLoginReq) {

		return userService.login(userLoginReq);
	}

	@PostMapping("/update/{id}")
	@Operation(summary = "유저 업데이트 (전달 안 한 속성은 업데이트 안함)")
	Api<?> update(@PathVariable("id") Long userId, @Valid @RequestBody UserUpdateReq userUpdateReq) {

		return userService.updateByUserId(userId, userUpdateReq);
	}
}
