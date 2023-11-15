package com.ssafy.dog.domain.chat.repository;

import static com.ssafy.dog.domain.chat.entity.QChatMembers.*;

import java.util.List;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.dog.common.error.ChatErrorCode;
import com.ssafy.dog.common.exception.ApiException;
import com.ssafy.dog.domain.chat.entity.ChatMembers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class ChatMembersRepositoryImpl implements ChatMembersRepositoryCustom {
	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<ChatMembers> getChatMembersList(Long userId) {
		return jpaQueryFactory
			.select(chatMembers)
			.from(chatMembers)
			.where(chatMembers.user.userId.eq(userId))
			.fetch();
	}

	@Override
	public ChatMembers findByRoomIdAndUserId(Long roomId, Long userId) {
		ChatMembers chatMember = jpaQueryFactory
			.select(chatMembers)
			.from(chatMembers)
			.where(chatMembers.user.userId.eq(userId).and(chatMembers.chatRoom.roomId.eq(roomId)))
			.fetchOne();

		if (chatMember == null) {
			throw new ApiException(ChatErrorCode.CHATROOM_NOT_FOUND);
		}
		return chatMember;
	}

}
