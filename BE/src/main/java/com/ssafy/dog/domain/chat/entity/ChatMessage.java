package com.ssafy.dog.domain.chat.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ChatMessage {

	@Id
	private Long chatId;

	private String chatContent;

	private String chatCreatedAt;
}
