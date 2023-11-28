package com.ssafy.dog.domain.chat.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.dog.common.error.ChatErrorCode;
import com.ssafy.dog.common.exception.ApiException;
import com.ssafy.dog.domain.chat.entity.ChatMembers;
import com.ssafy.dog.domain.chat.entity.mongo.ChatRead;
import com.ssafy.dog.domain.chat.entity.redis.ChatRoomUsers;
import com.ssafy.dog.domain.chat.repository.ChatMembersRepository;
import com.ssafy.dog.domain.chat.repository.mongo.ChatReadRepository;
import com.ssafy.dog.domain.chat.repository.redis.ChatRoomUsersRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ChatRoomService {

	private final ChatRoomUsersRepository chatRoomUsersRepository;
	private final ChatMembersRepository chatMembersRepository;
	private final ChatReadRepository chatReadRepository;

	@Transactional
	public void connectChatRoom(Long chatRoomId, Long userId) {

		Optional<ChatRoomUsers> chatRoomUsers = chatRoomUsersRepository.findByChatRoomIdAndUserId(chatRoomId, userId);

		if (!chatRoomUsers.isPresent()) {
			ChatRoomUsers newChatRoomUsers = ChatRoomUsers.builder().userId(userId).chatRoomId(chatRoomId).build();

			log.info("Redis 접속 저장 : {}", newChatRoomUsers.getUserId());
			chatRoomUsersRepository.save(newChatRoomUsers);
		}

	}

	@Transactional
	public void disconnectChatRoom(Long chatRoomId, Long userId) {
		ChatRoomUsers chatRoomUsers = chatRoomUsersRepository.findByChatRoomIdAndUserId(chatRoomId, userId)
			.orElseThrow(() -> new ApiException(ChatErrorCode.ROOM_USER_NOT_FOUND));

		chatRoomUsersRepository.delete(chatRoomUsers);
	}

	public List<Long> isConnected(Long chatRoomId) {

		List<ChatRoomUsers> connectedUser = chatRoomUsersRepository.findByChatRoomId(chatRoomId);

		List<Long> userIdList = new ArrayList<>();

		for (ChatRoomUsers conUser : connectedUser) {
			userIdList.add(conUser.getUserId());
		}
		// return chatRoomUsersRepository.findByChatRoomId(chatRoomId);
		return userIdList;
	}

	@Transactional
	public void leaveChatRoom(Long roomId, Long userId) {

		ChatMembers chatMembers = chatMembersRepository.findByRoomIdAndUserId(roomId, userId);

		chatMembersRepository.delete(chatMembers);

		// ChatRead의 readList에서 해당 userId 제거
		List<ChatRead> chatReadList = chatReadRepository.findByRoomId(roomId);
		List<ChatRead> changeReads = new ArrayList<>();
		if (!chatReadList.isEmpty()) {
			log.info("readList size : {}", chatReadList.size());

			for (ChatRead chatRead : chatReadList) {
				List<Long> curRead = chatRead.getReadList();
				log.info("읽은 목록 : {}", curRead);

				// readList에서 userId 제거
				if (curRead.contains(userId)) {
					log.info("readList에 포함되어있음");
					curRead.remove(userId);
					chatRead.builder().readList(curRead).build();
					changeReads.add(chatRead);
				}

			}
			chatReadRepository.saveAll(changeReads);

		}

		// 현재 채팅방 접속중인 사람들의 userId 리스트 변경하기
	}

}
