package com.ssafy.dog.domain.fcm.service;

import org.springframework.stereotype.Service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class FirebaseService {

	public String sendNotification(String content, String token) {
		try {
			Message message = Message.builder()
				.setToken(token)
				.putData("content", content)
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
}
