package com.ssafy.dog.domain.chat.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.dog.common.api.Api;
import com.ssafy.dog.common.error.UserErrorCode;
import com.ssafy.dog.common.exception.ApiException;
import com.ssafy.dog.domain.chat.dto.MessageDto;
import com.ssafy.dog.domain.chat.dto.req.ChatRoomReqDto;
import com.ssafy.dog.domain.chat.dto.res.ChatListResDto;
import com.ssafy.dog.domain.chat.entity.ChatMembers;
import com.ssafy.dog.domain.chat.entity.ChatRoom;
import com.ssafy.dog.domain.chat.entity.mongo.ChatHistory;
import com.ssafy.dog.domain.chat.entity.mongo.ChatRead;
import com.ssafy.dog.domain.chat.repository.ChatMembersRepository;
import com.ssafy.dog.domain.chat.repository.ChatRoomRepository;
import com.ssafy.dog.domain.chat.repository.mongo.ChatHistoryRepository;
import com.ssafy.dog.domain.chat.repository.mongo.ChatReadRepository;
import com.ssafy.dog.domain.chat.repository.redis.ChatRoomUsersRepository;
import com.ssafy.dog.domain.chat.util.KafkaConstants;
import com.ssafy.dog.domain.user.entity.User;
import com.ssafy.dog.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ChatService {

	private final KafkaProducerService kafkaProducerService;

	private final ChatRoomService chatRoomService;

	private final UserRepository userRepository;
	private final ChatRoomRepository chatRoomRepository;
	private final ChatMembersRepository chatMembersRepository;
	private final ChatHistoryRepository chatHistoryRepository;
	private final ChatRoomUsersRepository chatRoomUsersRepository;
	private final ChatReadRepository chatReadRepository;

	// @Transactional
	// public Api<?> createChatRoom(ChatRoomReqDto chatRoomReqDto) {
	// 	ChatRoom chatRoom = ChatRoom.builder().build();
	// 	chatRoomRepository.save(chatRoom);
	// 	for (String nickName : chatRoomReqDto.getUserNicks()) {
	// 		User user = userRepository.findByUserNickname(nickName)
	// 			.orElseThrow(() -> new ApiException(UserErrorCode.USER_NOT_FOUND));
	//
	// 		ChatMembers chatMembers = ChatMembers.builder()
	// 			.chatRoom(chatRoom)
	// 			.user(user)
	// 			.build();
	//
	// 		chatMembersRepository.save(chatMembers);
	//
	// 	}
	//
	// 	return Api.ok(chatRoom.getRoomId() + "채팅방 생성 성공");
	// }

	@Transactional
	public Api<?> createChatRoom(ChatRoomReqDto chatRoomReqDto) {
		List<User> users = new ArrayList<>();
		for (String nickName : chatRoomReqDto.getUserNicks()) {
			User user = userRepository.findByUserNickname(nickName)
				.orElseThrow(() -> new ApiException(UserErrorCode.USER_NOT_FOUND));
			users.add(user);
		}

		ChatRoom chatRoom = ChatRoom.builder().build();
		chatRoomRepository.save(chatRoom);

		List<ChatMembers> chatMembersList = new ArrayList<>();

		for (User user : users) {

			ChatMembers chatMembers = ChatMembers.builder()
				.chatRoom(chatRoom)
				.user(user)
				.build();

			chatMembersList.add(chatMembers);
		}
		chatMembersRepository.saveAll(chatMembersList);

		return Api.ok(chatRoom.getRoomId() + "채팅방 생성 성공");
	}

	public Api<List<ChatListResDto>> getChatList(String accessToken) {
		log.info("getChatList 메소드");
		log.info("Access 토큰 : {}", accessToken);
		// 임시, accessToken 으로 userId 받을 예정
		// Long curUserId = Long.parseLong(accessToken);
		// log.info("userID 값 : {}", curUserId);
		Long curUserId = Long.valueOf(1);
		List<ChatListResDto> roomLists = chatRoomRepository.getUserChatRoomsAndUserNicknames(curUserId);
		// log.info("RoomID :{}", (chatMembersList.get(0).getChatRoom().getRoomId()));
		log.info("RoomID :{}", (roomLists.size()));

		return Api.ok(roomLists);
	}

	public Api<?> getChatHistory(Long roomId) {
		return Api.ok("채팅 내역");
	}

	public void sendMessage(MessageDto message, String accessToken) {
		// 메시지 전송 요청 헤더에 포함된 Access Token에서 email로 회원을 조회한다.
		// Member findMember = memberRepository.findByEmail(jwtUtil.getUid(accessToken))
		// 	.orElseThrow(IllegalStateException::new);

		/*
		AccessToken 검증 후 userId로 보내주기
		 */
		// read 한 사람들 추가
		message.setSendTimeAndSenderAndRead(LocalDateTime.now(), Long.valueOf(accessToken), message.getSenderName(), 10,
			chatRoomService.isConnected(message.getRoomId()));

		kafkaProducerService.send(KafkaConstants.KAFKA_TOPIC, message);

		// kafka producer -> consumer -> stomp converAndSend 까지 된 후 DB 저장로직
		saveChat(message);

	}

	// MongoDB ChatHistory 저장
	public void saveChat(MessageDto message) {
		// chatHistoryRepository.save(message.convertEntity());
		ChatHistory curChatHistory = message.convertEntity();
		chatHistoryRepository.save(curChatHistory);

		// Set<Long> connectedList = chatRoomService.isConnected(message.getRoomId());
		List<Long> connectedList = chatRoomService.isConnected(message.getRoomId());

		ChatRead curRead = ChatRead.builder()
			.historyId(curChatHistory.getHistoryId())
			.readList(connectedList)
			.build();

		chatReadRepository.save(curRead);

	}

}
