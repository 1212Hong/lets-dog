package com.ssafy.dog.domain.chat.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.ssafy.dog.common.api.Api;
import com.ssafy.dog.domain.chat.dto.MessageDto;
import com.ssafy.dog.domain.chat.repository.ChattingRepository;
import com.ssafy.dog.domain.chat.util.KafkaConstants;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ChatService {

	private final KafkaProducerService kafkaProducerService;
	private final ChattingRepository chattingRepository;

	public Api<?> createChat(MessageDto message) {
		chattingRepository.save(message.convertEntity());

		return Api.ok("채팅 저장 성공");
	}

	public void sendMessage(MessageDto message, String accessToken) {
		// 메시지 전송 요청 헤더에 포함된 Access Token에서 email로 회원을 조회한다.
		// Member findMember = memberRepository.findByEmail(jwtUtil.getUid(accessToken))
		// 	.orElseThrow(IllegalStateException::new);

		message.setSendTimeAndSender(LocalDateTime.now(), Long.valueOf(1), "머홍", 2);

		kafkaProducerService.send(KafkaConstants.KAFKA_TOPIC, message);
	}
}
