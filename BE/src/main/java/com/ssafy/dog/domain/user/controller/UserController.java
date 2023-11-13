package com.ssafy.dog.domain.user.controller;

import java.util.regex.Pattern;

import javax.validation.Valid;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
import com.ssafy.dog.util.SecurityUtils;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequestMapping("/api/user")
@RequiredArgsConstructor
@RestController
@Slf4j
public class UserController {
	private final UserService userService;
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");
	private static final Pattern PASSWORD_PATTERN = Pattern.compile(
		"^(?=.*[A-Z])(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,16}$");
	private static final Pattern PHONE_NUMBER_PATTERN = Pattern.compile("^\\d{10,11}$\n");

	@PostMapping("/signup")
	@Operation(summary = "회원가입")
	public Api<?> signUp(@Valid @RequestBody UserSignupReq userSignupReq, Errors errors) {

		return userService.create(userSignupReq, errors);
	}

	@PostMapping("/login")
	@Operation(summary = "로그인")
	public Api<?> login(@Valid @RequestBody UserLoginReq userLoginReq, Errors errors) {

		return userService.login(userLoginReq);
	}

	@PatchMapping("/update")
	@Operation(summary = "유저 업데이트 (우선 전달 안 한 값은 null로 들어감)")
	Api<?> update(@Valid @RequestBody UserUpdateReq userUpdateReq) {
		String nickname = SecurityUtils.getUser().getUserNickname();

		log.info("nickname : {}", nickname);

		return userService.updateByUserNickname(nickname, userUpdateReq);
	}

	@GetMapping("/get/{nickname}")
	@Operation(summary = "유저 정보 조회")
	Api<?> read(@PathVariable("nickname") String userNickname) {

		return userService.getByUserNickname(userNickname);
	}
}
