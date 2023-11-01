package com.ssafy.dog.domain.chat.config;

import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import com.google.common.collect.ImmutableMap;
import com.ssafy.dog.domain.chat.dto.MessageDto;
import com.ssafy.dog.domain.chat.util.KafkaConstants;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@EnableKafka
@Configuration
public class KafkaConsumerConfig {

	@Value("${my.kafkabroker}")
	private String kafkaBroker;

	// KafkaListener 컨테이너 팩토리를 생성하는 Bean 메서드
	@Bean
	ConcurrentKafkaListenerContainerFactory<String, MessageDto> kafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, MessageDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactory());
		return factory;
	}

	// Kafka ConsumerFactory를 생성하는 Bean 메서드
	@Bean
	public ConsumerFactory<String, MessageDto> consumerFactory() {
		JsonDeserializer<MessageDto> deserializer = new JsonDeserializer<>();
		// 패키지 신뢰 오류로 인해 모든 패키지를 신뢰하도록 작성
		deserializer.addTrustedPackages("*");

		// Kafka Consumer 구성을 위한 설정값들을 설정 -> 변하지 않는 값이므로 ImmutableMap을 이용하여 설정
		Map<String, Object> consumerConfigurations =
			ImmutableMap.<String, Object>builder()
				// .put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConstants.KAFKA_BROKER)    // 브로커 주소를 설정
				.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBroker)    // 브로커 주소를 설정
				.put(ConsumerConfig.GROUP_ID_CONFIG, KafkaConstants.GROUP_ID)
				.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class)
				.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer)
				.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest")
				.build();

		return new DefaultKafkaConsumerFactory<>(consumerConfigurations, new StringDeserializer(), deserializer);
	}
}
