package com.ssafy.dog.domain.fcm.service;

import org.springframework.stereotype.Service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.ssafy.dog.common.error.FCMErrorCode;
import com.ssafy.dog.common.exception.ApiException;
import com.ssafy.dog.domain.fcm.dto.FCMDto;
import com.ssafy.dog.domain.fcm.entity.FcmToken;
import com.ssafy.dog.domain.fcm.repository.FcmTokenRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class FirebaseService {

	private final FcmTokenRepository fcmTokenRepository;

	public String sendNotification(FCMDto fcmDto) {
		try {
			String token = getFcmToken(fcmDto.getUserId());
			Message message = Message.builder()
				.setToken(token)
				.putData("title", fcmDto.getTitle())
				.putData("content", fcmDto.getContent())
				.build();

			log.info("토큰 출력 : {}", token);
			log.info("메시지 전송 시도 : {}", message.toString());
			String response = FirebaseMessaging.getInstance().send(message);
			log.info("메시지 전송 성공 : {}", message.toString());

			// return response if firebase messaging is successfully completed.
			return response;

		} catch (FirebaseMessagingException e) {
			e.printStackTrace();
			return "Failed";
		}
	}

	private String getFcmToken(Long userId) {
		log.info("user PK : {}", userId);

		FcmToken fcmToken = fcmTokenRepository.findById(userId)
			.orElseThrow(() -> new ApiException(FCMErrorCode.FCM_TOKEN_NOT_FOUND));

		return fcmToken.getFcmToken();
	}
}
