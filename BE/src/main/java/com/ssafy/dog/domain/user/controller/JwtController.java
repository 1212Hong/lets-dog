package com.ssafy.dog.domain.user.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.dog.common.api.Api;

@RequestMapping("/jwt")
@RestController
public class JwtController {
	@GetMapping("/valid") // Header 에서 Authorization 하는 방식으로 바꾸기
	public Api<Object> valid(String jwt) {
		return Api.ok(1);
	}
}
