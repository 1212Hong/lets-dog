package com.ssafy.dog.domain.chat.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.dog.common.api.Api;
import com.ssafy.dog.common.error.UserErrorCode;
import com.ssafy.dog.common.exception.ApiException;
import com.ssafy.dog.domain.chat.dto.MessageDto;
import com.ssafy.dog.domain.chat.dto.req.ChatRoomReqDto;
import com.ssafy.dog.domain.chat.entity.ChatMembers;
import com.ssafy.dog.domain.chat.entity.ChatRoom;
import com.ssafy.dog.domain.chat.repository.ChatHistoryRepository;
import com.ssafy.dog.domain.chat.repository.ChatMembersRepository;
import com.ssafy.dog.domain.chat.repository.ChatRoomRepository;
import com.ssafy.dog.domain.chat.util.KafkaConstants;
import com.ssafy.dog.domain.user.entity.User;
import com.ssafy.dog.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ChatService {

	private final KafkaProducerService kafkaProducerService;

	private final UserRepository userRepository;
	private final ChatRoomRepository chatRoomRepository;
	private final ChatMembersRepository chatMembersRepository;
	private final ChatHistoryRepository chatHistoryRepository;

	@Transactional
	public Api<?> createChatRoom(ChatRoomReqDto chatRoomReqDto) {
		ChatRoom chatRoom = ChatRoom.builder().build();
		chatRoomRepository.save(chatRoom);
		for (String nickName : chatRoomReqDto.getUserNicks()) {
			User user = userRepository.findByUserNickname(nickName)
				.orElseThrow(() -> new ApiException(UserErrorCode.USER_NOT_FOUND));

			ChatMembers chatMembers = ChatMembers.builder()
				.chatRoom(chatRoom)
				.user(user)
				.build();

			chatMembersRepository.save(chatMembers);

		}

		return Api.ok(chatRoom.getRoomId() + "채팅방 생성 성공");
	}

	public Api<?> getChatList() {
		Long curUserId = Long.valueOf(1); // 임시, accessToken 으로 받을 예정
		List<ChatMembers> chatMembersList = chatMembersRepository.getChatMembersList(curUserId);

		return Api.ok(chatMembersList);
	}

	public Api<?> getChatHistory(Long roomId) {
		return Api.ok("채팅 내역");
	}

	public Api<?> createChat(MessageDto message) {
		chatHistoryRepository.save(message.convertEntity());

		return Api.ok("채팅 저장 성공");
	}

	public void sendMessage(MessageDto message, String accessToken) {
		// 메시지 전송 요청 헤더에 포함된 Access Token에서 email로 회원을 조회한다.
		// Member findMember = memberRepository.findByEmail(jwtUtil.getUid(accessToken))
		// 	.orElseThrow(IllegalStateException::new);

		message.setSendTimeAndSender(LocalDateTime.now(), Long.valueOf(1), "머홍", 2);

		kafkaProducerService.send(KafkaConstants.KAFKA_TOPIC, message);

		// kafka producer -> consumer -> stomp converAndSend 까지 된 후 DB 저장로직

	}

}
