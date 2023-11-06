package com.ssafy.dog.security.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.dog.common.api.Api;
import com.ssafy.dog.common.error.ErrorCode;
import com.ssafy.dog.common.exception.ApiException;
import com.ssafy.dog.security.JwtTokenProvider;

@RequestMapping("/jwt")
@RestController
public class JwtController {

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@GetMapping("/valid") //
	public Api<Object> valid(HttpServletRequest request) {
		String tokenHeader = request.getHeader("Authorization");

		if (tokenHeader != null && tokenHeader.startsWith("Bearer ")) {
			String jwt = tokenHeader.substring(7);

			if (jwtTokenProvider.validateToken(jwt)) {
				return Api.ok(1);
			}
		}

		throw new ApiException(ErrorCode.FORBIDDEN);
	}
}
