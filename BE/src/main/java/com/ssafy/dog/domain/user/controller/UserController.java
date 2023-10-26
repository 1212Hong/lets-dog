package com.ssafy.dog.domain.user.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.dog.domain.user.dto.UserDto;
import com.ssafy.dog.domain.user.repository.UserRepository;
import com.ssafy.dog.domain.user.service.UserService;

@RequestMapping("/user")
@RestController
public class UserController {
	private final UserService userService;
	private final UserRepository userRepository;

	@Autowired
	public UserController(UserService userService, UserRepository userRepository) { // <- RequiredArgsConstructor
		this.userService = userService;
		this.userRepository = userRepository;
	}

	@PostMapping("/sign-up")
	@ResponseStatus(HttpStatus.OK)
	public Long signUp(@Valid @RequestBody UserDto form) throws Exception {
		return userService.signUp(form);
	}
}
