package com.ssafy.dog.domain.chat.config;

import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import com.google.common.collect.ImmutableMap;
import com.ssafy.dog.domain.chat.dto.MessageDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@EnableKafka
@Configuration
public class KafkaProducerConfig {

	@Value("${my.kafkabroker}")
	private String kafkaBroker;

	// Kafka ProducerFactory를 생성하는 Bean 메서드
	@Bean
	public ProducerFactory<String, MessageDto> producerFactory() {
		return new DefaultKafkaProducerFactory<>(producerConfigurations());
	}

	// Kafka Producer 구성을 위한 설정값들을 포함한 맵을 반환하는 메서드
	@Bean
	public Map<String, Object> producerConfigurations() {
		return ImmutableMap.<String, Object>builder()
			// .put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConstants.KAFKA_BROKER)    // 브로커 주소를 설정
			.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBroker)    // 브로커 주소를 설정
			.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
				StringSerializer.class)    // 키를 어떤 Serializer를 사용해서 설정 -> String
			.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class)    // Value -> Json
			.build();
	}

	// KafkaTemplate을 생성하는 Bean 메서드
	@Bean
	public KafkaTemplate<String, MessageDto> kafkaTemplate() {
		return new KafkaTemplate<>(producerFactory());
	}
}
