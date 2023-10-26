package com.ssafy.dog.domain.chat.repository;

import static com.ssafy.dog.domain.chat.entity.QChatMessage.*;

import java.util.List;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.dog.domain.chat.entity.ChatMessage;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ChatMessageRepositoryImpl implements ChatMessageRepositoryCustom {
	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<ChatMessage> getAllMessage() {
		return jpaQueryFactory.selectFrom(chatMessage)
			.fetch();
	}

}
