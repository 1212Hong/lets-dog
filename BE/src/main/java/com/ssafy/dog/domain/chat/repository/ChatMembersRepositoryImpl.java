package com.ssafy.dog.domain.chat.repository;

import static com.ssafy.dog.domain.chat.entity.QChatMembers.*;

import java.util.List;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.dog.domain.chat.entity.ChatMembers;

import lombok.RequiredArgsConstructor;

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

}
