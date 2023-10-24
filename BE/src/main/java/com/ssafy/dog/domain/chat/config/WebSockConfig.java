package com.ssafy.dog.domain.chat.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import com.ssafy.dog.domain.chat.filter.StompHandler;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@EnableWebSocketMessageBroker // Stomp를 사용하기 위한 에노테이션
@Configuration
public class WebSockConfig implements WebSocketMessageBrokerConfigurer {

	private final StompHandler stompHandler;

	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
		config.enableSimpleBroker("/sub"); // 메세지를 구독하는 요청 설정
		config.setApplicationDestinationPrefixes("/pub"); // 메세지를 발행하는 요청 설정
	}

	/*
	WebSocket 엔드포인트로 등록한 경로는 반드시 Spring Security 설정에서 접근 가능하도록 등록해야 한다.
	참고로 웹이 아닌 앱을 통한 채팅 기능을 구현할 때 withSockJS()를 사용하면 동작하지 않는다.
	 */
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/ws-stomp").setAllowedOrigins("*")
			.withSockJS(); // sock.js를 통하여 낮은 버전의 브라우저에서도 websocket이 동작할수 있게 설정
		registry.addEndpoint("/ws-stomp").setAllowedOrigins("*"); // api 통신 시, withSockJS() 설정을 빼야됨
	}

	@Override
	public void configureClientInboundChannel(ChannelRegistration registration) {
		registration.interceptors(stompHandler);
	}
}
