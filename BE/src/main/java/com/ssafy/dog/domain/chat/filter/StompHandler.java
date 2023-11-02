package com.ssafy.dog.domain.chat.filter;

import java.util.List;
import java.util.Objects;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import com.ssafy.dog.domain.chat.service.ChatRoomService;
import com.ssafy.dog.domain.chat.service.ChatService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 99)// 우선 순위를 높게 설정해서 SecurityFilter들 보다 앞서 실행되게 해준다.
public class StompHandler implements ChannelInterceptor {

	private final ChatService chatService;
	private final ChatRoomService chatRoomService;

	// private final JwtUtil jwtUtil;

	/*
	WebSocket을 통해 들어온 요청이 처리되기 전에 실행된다.
	이 메서드는 수신된 메시지를 가로채고 처리하기 전에 사전 처리 작업을 수행
	WebSocket을 통해 들어온 요청의 Stomp 헤더를 가로채어 JWT 토큰의 유효성을 검증하는 역할을 수행
	 */
	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

		// AccessToken 유효성 검증
		// Long userId = verifyAccessToken(getAccessToken(accessor));
		// log.info("StompAccessor = {}", accessor);
		// StompCommand에 따라서 로직을 분기해서 처리하는 메서드를 호출
		// handleMessage(Objects.requireNonNull(accessor.getCommand()), accessor, userId);
		handleMessage(Objects.requireNonNull(accessor.getCommand()), accessor);

		// // websocket 연결시 헤더의 jwt token 유효성 검증
		// if (StompCommand.CONNECT == accessor.getCommand()) {
		// 	log.info("jwt token 유효성 메소드 진입");
		// 	// if (StompCommand.CONNECT == accessor.getCommand()) {
		// 	// 	String authorization = jwtUtil.extractJwt(accessor.getFirstNativeHeader("Authorization"));
		// 	// 	jwtUtil.parseClaims(authorization);
		// 	// }
		//
		// 	String authToken = accessor.getFirstNativeHeader("Authorization");
		// 	// if (authToken == null || !jwtProvider.validateJwt(authToken)) {
		//
		// 	if (authToken == null) {
		// 		throw new ApiException(ChatErrorCode.JWT_NOT_FOUND);
		// 	}
		// }
		return message;
	}

	public void handleMessage(StompCommand stompCommand, StompHeaderAccessor accessor) {
		log.info("Command 종류 = {}", stompCommand);
		switch (stompCommand) {
			case CONNECT:
				// AccessToken 유효성 검증
				Long userId = verifyAccessToken(getAccessToken(accessor));
				connectToChatRoom(accessor, userId);
				break;
			case SUBSCRIBE:
				break;
			case SEND:
				verifyAccessToken(getAccessToken(accessor));
				break;
		}
	}

	private void connectToChatRoom(StompHeaderAccessor accessor, Long userId) {
		// 채팅방 번호를 가져온다.
		Long chatRoomId = getChatRoomId(accessor);

		// 채팅방 입장 처리 -> Redis에 입장 내역 저장
		chatRoomService.connectChatRoom(chatRoomId, userId);

		// 읽지 않은 채팅을 전부 읽음 처리
		// chatService.updateCountAllZero(chatRoomNo, email);

		// 현재 채팅방에 접속중인 인원이 있는지 확인한다.
		List<Long> connectedList = chatRoomService.isConnected(chatRoomId);

		int headCnt = connectedList.size();
		// if (isConnected) {
		// 	chatService.updateMessage(email, chatRoomNo);
		// }
	}

	private Long verifyAccessToken(String accessToken) {

		// if (!jwtUtil.verifyToken(accessToken)) {
		// 	throw new IllegalStateException("토큰이 만료되었습니다.");
		// }
		//
		// return jwtUtil.getUid(accessToken);
		return Long.parseLong(accessToken);
		// return Long.valueOf(1);

	}

	private String getAccessToken(StompHeaderAccessor accessor) {
		String authToken = accessor.getFirstNativeHeader("Authorization");
		log.info("토큰 추출 : {}", authToken);
		// if (authToken == null || !jwtProvider.validateJwt(authToken)) {

		return authToken;
	}

	private Long getChatRoomId(StompHeaderAccessor accessor) {
		return
			Long.valueOf(
				Objects.requireNonNull(
					accessor.getFirstNativeHeader("Chatroom-no")

				));
	}

}
