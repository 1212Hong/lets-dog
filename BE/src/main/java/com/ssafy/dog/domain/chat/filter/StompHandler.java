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

import com.ssafy.dog.common.error.JWTErrorCode;
import com.ssafy.dog.common.exception.ApiException;
import com.ssafy.dog.domain.chat.service.ChatRoomService;
import com.ssafy.dog.domain.chat.service.ChatService;
import com.ssafy.dog.security.JwtTokenProvider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 99)// 우선 순위를 높게 설정해서 SecurityFilter들 보다 앞서 실행되게 해준다.
public class StompHandler implements ChannelInterceptor {

	private final ChatService chatService;
	private final ChatRoomService chatRoomService;
	private final JwtTokenProvider jwtTokenProvider;

	/*
	WebSocket을 통해 들어온 요청이 처리되기 전에 실행된다.
	이 메서드는 수신된 메시지를 가로채고 처리하기 전에 사전 처리 작업을 수행
	WebSocket을 통해 들어온 요청의 Stomp 헤더를 가로채어 JWT 토큰의 유효성을 검증하는 역할을 수행
	 */
	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

		handleMessage(Objects.requireNonNull(accessor.getCommand()), accessor);

		return message;
	}

	public void handleMessage(StompCommand stompCommand, StompHeaderAccessor accessor) {
		log.info("Command 종류 = {}", stompCommand);
		switch (stompCommand) {
			case CONNECT:
				// AccessToken 유효성 검증
				Long userId = verifyAccessToken(getAccessToken(accessor));
				// 임시로 랜덤생성
				// Long userId = Long.valueOf(ThreadLocalRandom.current().nextInt(1, 4));
				// Long userId = Long.valueOf(7);

				connectToChatRoom(accessor, userId);
				break;
			case SUBSCRIBE:
				break;
			case SEND:
				break;
		}
	}

	private void connectToChatRoom(StompHeaderAccessor accessor, Long userId) {

		// 채팅방 번호를 가져온다.
		Long chatRoomId = getChatRoomId(accessor);

		// 임시 구현
		// Long chatRoomId = Long.valueOf(1);
		log.info("채팅방 접속 성공 : {}, 유저Id : {}", chatRoomId, userId);

		// 채팅방 입장 처리 -> Redis에 입장 내역 저장
		chatRoomService.connectChatRoom(chatRoomId, userId);

		// 현재 채팅방에 접속중인 인원이 있는지 확인한다.
		List<Long> connectedList = chatRoomService.isConnected(chatRoomId);
		log.info("접속중인 채팅방 유저 목록 : {}", connectedList);
		int headCnt = connectedList.size();

		// 나 자신을 제외 했을때 1명보다 많은 경우 Notice 알림
		if (headCnt > 1) {
			chatService.sendNotice(chatRoomId, userId);
		}

	}

	private Long verifyAccessToken(String accessToken) {
		String jwt = accessToken.substring(7);

		if (!jwtTokenProvider.validateToken(jwt)) {
			throw new ApiException(JWTErrorCode.JWT_TOKEN_NOT_VALID);
		}
		return jwtTokenProvider.getJwtUserId(jwt);

	}

	private String getAccessToken(StompHeaderAccessor accessor) {
		String authToken = accessor.getFirstNativeHeader("Authorization");
		log.info("토큰 추출 : {}", authToken);

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
