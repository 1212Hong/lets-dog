package com.ssafy.dog.domain.chat.repository;

import static com.ssafy.dog.domain.chat.entity.QChatMembers.*;
import static com.ssafy.dog.domain.chat.entity.QChatRoom.*;
import static com.ssafy.dog.domain.user.entity.QUser.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.dog.domain.chat.dto.res.ChatListResDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ChatRoomRepositoryImpl implements ChatRoomRepositoryCustom {
	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<ChatListResDto> getUserChatRoomsAndUserNicknames(Long userId) {

		List<Tuple> result = jpaQueryFactory
			.select(
				chatRoom.roomId,
				user.userNickname
			)
			.from(chatRoom)
			.innerJoin(chatRoom.chatMembers, chatMembers)
			.innerJoin(chatMembers.user, user)
			.where(chatMembers.chatRoom.in(
				JPAExpressions
					.select(chatRoom)
					.from(chatRoom)
					.innerJoin(chatRoom.chatMembers, chatMembers)
					.where(chatMembers.user.userId.eq(userId)
					)
			))
			.fetch();

		Map<Long, List<String>> userNicknamesMap = new HashMap<>();

		for (Tuple tuple : result) {
			Long roomId = tuple.get(chatRoom.roomId);
			String userNickname = tuple.get(user.userNickname);

			userNicknamesMap.computeIfAbsent(roomId, k -> new ArrayList<>()).add(userNickname);
		}

		List<ChatListResDto> dtos = new ArrayList<>();

		for (Map.Entry<Long, List<String>> entry : userNicknamesMap.entrySet()) {
			dtos.add(new ChatListResDto(entry.getKey(), entry.getValue()));
		}

		return dtos;

	}
}
