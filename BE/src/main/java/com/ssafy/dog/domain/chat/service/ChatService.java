package com.ssafy.dog.domain.chat.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.dog.common.api.Api;
import com.ssafy.dog.common.error.UserErrorCode;
import com.ssafy.dog.common.exception.ApiException;
import com.ssafy.dog.domain.chat.dto.MessageDto;
import com.ssafy.dog.domain.chat.dto.NoticeDto;
import com.ssafy.dog.domain.chat.dto.req.ChatRoomReqDto;
import com.ssafy.dog.domain.chat.dto.res.ChatHistoriesResDto;
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

			ChatMembers chatMembers = ChatMembers.builder().chatRoom(chatRoom).user(user).build();

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

	public Api<List<ChatHistoriesResDto>> getChatHistory(Long roomId, String accessToken) {

		Long userId = Long.parseLong(accessToken);
		log.info("유저 PK : {}", userId);
		// 채팅 내역 불러오기
		List<ChatHistory> chatHistories = chatHistoryRepository.findAllByRoomId(roomId);
		log.info("채팅 history : {}", chatHistories.size());
		List<ChatHistoriesResDto> chatHistoriesResDtos = new ArrayList<>();

		for (ChatHistory history : chatHistories) {

			// ChatRead 정보 조회
			Optional<ChatRead> chatRead = chatReadRepository.findByHistoryId(history.getHistoryId());
			// history와 readList로 Dto 생성
			chatRead.ifPresent(read -> {
				log.info("ChatRead 기록 있음");
				log.info("기존 readList : {}", read.getReadList());
				List<Long> readList = readCheck(read, userId);
				log.info("갱신된 readList : {}", readList);
				ChatHistoriesResDto resDto = new ChatHistoriesResDto(history, readList);
				chatHistoriesResDtos.add(resDto);
				log.info("길이 + {}", chatHistoriesResDtos.size());
			});

		}
		log.info("결과 + {}", chatHistoriesResDtos.size());
		log.info("값 + {}", chatHistoriesResDtos.get(0).toString());

		return Api.ok(chatHistoriesResDtos);
	}

	@Transactional
	public List<Long> readCheck(ChatRead chatRead, Long userId) {

		// 기존에 없을경우 readList 변경 후 저장
		if (!chatRead.getReadList().contains(userId)) {
			chatRead.getReadList().add(userId);
			chatReadRepository.save(chatRead);
		}
		return chatRead.getReadList();
	}

	@Transactional
	public void sendMessage(MessageDto message, String accessToken) {
		// 메시지 전송 요청 헤더에 포함된 Access Token에서 email로 회원을 조회한다.
		// Member findMember = memberRepository.findByEmail(jwtUtil.getUid(accessToken))
		// 	.orElseThrow(IllegalStateException::new);
		log.info("채팅전송 토큰 : {}", accessToken);
		/*
		AccessToken 검증 후 userId로 보내주기
		 */
		// read 한 사람들 추가
		message.setSendTimeAndSenderAndRead(LocalDateTime.now(), Long.parseLong(accessToken), message.getSenderName(),
			10, chatRoomService.isConnected(message.getRoomId()));

		kafkaProducerService.send(KafkaConstants.KAFKA_CHAT_TOPIC, message);

		// kafka producer -> consumer -> stomp converAndSend 까지 된 후 DB 저장로직
		saveChat(message);
	}

	public ChatHistory getTest() {
		List<ChatHistory> histories = chatHistoryRepository.findAll();
		return histories.get(0);
	}

	// MongoDB ChatHistory 저장
	public void saveChat(MessageDto message) {

		ChatHistory curChatHistory = message.convertEntity();
		ChatHistory savedEntity = chatHistoryRepository.save(curChatHistory);
		log.info("저장된 ChatHistory : {}", savedEntity.toString());
		String historyId = savedEntity.getHistoryId();

		List<Long> connectedList = chatRoomService.isConnected(message.getRoomId());
		log.info("채팅 읽은 사람들 : {}", connectedList);
		log.info("히스토리 PK 값 : {}", historyId);
		ChatRead curRead = ChatRead.builder().
			historyId(historyId).
			readList(connectedList).build();

		chatReadRepository.save(curRead);

	}

	@Transactional
	public void sendNotice(Long roomId, Long userId) {

		NoticeDto curNotice = new NoticeDto(roomId, userId);

		kafkaProducerService.sendNotice(KafkaConstants.KAFKA_NOTICE_TOPIC, curNotice);

		// kafka producer -> consumer -> stomp converAndSend 까지 된 후 DB 저장로직
		// saveChat(message);
	}

}
