package com.ssafy.dog.security.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.dog.common.api.Api;

@RequestMapping("/jwt")
@RestController
public class JwtController {
	/*
	 * Header 에서 Authorization 하는 방식으로 바꾸기 (Postman Headers 에서
	 * Authorization 에서 Value 가 검증 되면 1 보내주는 걸로 바꾸기)
	 **/
	@GetMapping("/valid") //
	public Api<Object> valid(String jwt) {
		return Api.ok(1);
	}
}
