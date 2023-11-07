package com.ssafy.dog.domain.matching.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.dog.common.api.Api;
import com.ssafy.dog.domain.matching.dto.MatchingUserResponse;
import com.ssafy.dog.domain.matching.service.MatchingService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class MatchingController {

	private final MatchingService matchingService;

	@GetMapping("/matching")
	public Api<List<MatchingUserResponse>> matchingUsers() {
		return Api.ok(matchingService.matchingUser()
			.stream()
			.map(MatchingUserResponse::toResponse)
			.collect(Collectors.toList()));
	}
}
