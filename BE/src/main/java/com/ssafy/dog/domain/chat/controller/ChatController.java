package com.ssafy.dog.domain.chat.controller;

import javax.validation.Valid;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.dog.common.api.Api;
import com.ssafy.dog.domain.chat.dto.ChatMessage;
import com.ssafy.dog.domain.chat.dto.MessageDto;
import com.ssafy.dog.domain.chat.dto.req.ChatRoomReqDto;
import com.ssafy.dog.domain.chat.service.ChatService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ChatController {

	private final ChatService chatService;

	private final SimpMessageSendingOperations messagingTemplate;
	// private final JwtUtil jwtUtil;

	@PostMapping("/chatroom")
	public Api<?> createChatRoom(@RequestBody ChatRoomReqDto chatRoomReqDto) {
		return chatService.createChatRoom(chatRoomReqDto);
	}

	@GetMapping("/chatroom")
	public Api<?> getChatRoomList() {
		return chatService.getChatList();
	}

	@GetMapping("/chatroom/{roomId}")
	public Api<?> getChatHistory(@PathVariable Long roomId) {
		return chatService.getChatHistory(roomId);
	}

	@CrossOrigin
	@MessageMapping("/chat/message") //websocket "/pub/chat/message"로 들어오는 메시지 처리
	// public void message(ChatMessage message, @Header("Authorization") String Authorization) {
	public void message(ChatMessage message) {

		log.info("메시지" + message);
		// String authorization = jwtUtil.extractJwt(Authorization);
		// Object memberId = jwtUtil.parseClaims(authorization).get("memberId");
		String memberId = "1";
		message.setSender(memberId);

		// /sub/chatroom/{roomId} - 구독
		messagingTemplate.convertAndSend("/sub/chatroom/" + message.getRoomId(), message);

	}

	// @MessageMapping("/message")
	// public void sendMessage(@Valid MessageDto message, @Header("Authorization") final String accessToken) {
	// 	chatService.sendMessage(message, accessToken);
	// }

	@MessageMapping("/message")
	public void sendMessage(@Valid MessageDto message) {
		chatService.sendMessage(message, "임시 토큰");
	}

	@PostMapping("/chat/test")
	public Api<?> createUser(@RequestBody MessageDto message) {
		return chatService.createChat(message);
	}

}