package com.ssafy.dog.domain.chat.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.ssafy.dog.domain.chat.dto.MessageDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class KafkaProducerService {

	private final KafkaTemplate<String, MessageDto> kafkaTemplate;

	// 메시지를 지정한 Kafka 토픽으로 전송
	public void send(String topic, MessageDto messageDto) {
		log.info("topic : " + topic);
		log.info("send Message : " + messageDto.getContent());
		// KafkaTemplate을 사용하여 메시지를 지정된 토픽으로 전송
		kafkaTemplate.send(topic, messageDto);
	}
}
