package com.ssafy.dog.domain.chat.filter;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import com.ssafy.dog.common.error.ChatErrorCode;
import com.ssafy.dog.common.exception.ApiException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class StompHandler implements ChannelInterceptor {

	// private final JwtUtil jwtUtil;

	/*
	WebSocket을 통해 들어온 요청이 처리되기 전에 실행된다.
	이 메서드는 수신된 메시지를 가로채고 처리하기 전에 사전 처리 작업을 수행
	WebSocket을 통해 들어온 요청의 Stomp 헤더를 가로채어 JWT 토큰의 유효성을 검증하는 역할을 수행
	 */
	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
		// websocket 연결시 헤더의 jwt token 유효성 검증
		if (StompCommand.CONNECT == accessor.getCommand()) {
			log.info("jwt token 유효성 메소드 진입");
			// if (StompCommand.CONNECT == accessor.getCommand()) {
			// 	String authorization = jwtUtil.extractJwt(accessor.getFirstNativeHeader("Authorization"));
			// 	jwtUtil.parseClaims(authorization);
			// }

			String authToken = accessor.getFirstNativeHeader("Authorization");
			// if (authToken == null || !jwtProvider.validateJwt(authToken)) {

			if (authToken == null) {
				throw new ApiException(ChatErrorCode.JWT_NOT_FOUND);
			}
		}
		return message;
	}
}
