package com.ssafy.dog.domain.chat.service;

import java.io.IOException;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import com.ssafy.dog.domain.chat.dto.MessageDto;
import com.ssafy.dog.domain.chat.util.KafkaConstants;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class KafkaConsumerService {

	private final SimpMessageSendingOperations template;

	@KafkaListener(topics = KafkaConstants.KAFKA_TOPIC, groupId = KafkaConstants.GROUP_ID)
	public void receive(MessageDto message) throws IOException {

		// log.info("Consumed Message : " + message.getMessage());
		// HashMap<String, String> msg = new HashMap<>();
		// msg.put("roomId", String.valueOf(message.getRoomId()));
		// msg.put("message", message.getMessage());
		// msg.put("writer", message.getWriter());
		//
		// ObjectMapper mapper = new ObjectMapper();
		// template.convertAndSend("/topic/tt", mapper.writeValueAsString(msg));

		log.info("전송 위치 = /sub/chatroom/" + message.getRoomId());
		log.info("채팅 방으로 메시지 전송 = {}", message.toString());

		// /sub/chatroom/{roomId} - 구독
		// 메시지객체 내부의 채팅방번호를 참조하여, 해당 채팅방 구독자에게 메시지를 발송한다.
		template.convertAndSend("/sub/chatroom/" + message.getRoomId(), message);
	}
}
