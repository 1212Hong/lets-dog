package com.ssafy.dog.domain.matching.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

	@GetMapping("/matching/{userId}")
	public Api<List<MatchingUserResponse>> matchingUsers(@PathVariable String userId) {
		return Api.ok(matchingService.matchingUser(userId)
			.stream()
			.map(MatchingUserResponse::toResponse)
			.collect(Collectors.toList()));
	}
}
