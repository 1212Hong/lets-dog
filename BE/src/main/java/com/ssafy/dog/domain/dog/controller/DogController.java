package com.ssafy.dog.domain.dog.controller;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.dog.common.api.Api;
import com.ssafy.dog.domain.dog.dto.request.DogCreateReq;
import com.ssafy.dog.domain.dog.entity.Dog;
import com.ssafy.dog.domain.dog.service.DogService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RequestMapping("/api/dog")
@RequiredArgsConstructor
@RestController
public class DogController {

	private final DogService dogService;

	@PostMapping
	@Operation(summary = "개 생성")
	public Api<?> createNewDog(@Valid @RequestBody DogCreateReq dogCreateReq) {
		Dog newDog = dogService.create(dogCreateReq);

		return Api.ok("강아지 생성 dog_id: " + newDog.getDogId());
	}
}