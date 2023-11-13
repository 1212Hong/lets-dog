package com.ssafy.dog.domain.fcm.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.dog.common.api.Api;
import com.ssafy.dog.domain.fcm.dto.FCMDto;
import com.ssafy.dog.domain.fcm.service.FirebaseService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
@RequestMapping("/fcm")
@RestController
public class FcmTestController {

	private final FirebaseService firebaseService;

	@PostMapping
	public Api<String> createNotification(@RequestBody FCMDto fcmTestDto) {
		log.info("시작");
		String response = firebaseService.sendNotification(fcmTestDto);
		log.info(response);
		return Api.ok(response);
	}

}