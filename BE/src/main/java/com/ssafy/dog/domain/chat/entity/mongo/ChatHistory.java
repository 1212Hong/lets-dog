package com.ssafy.dog.domain.chat.entity.mongo;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(collection = "chat_history")
// MongoDB Chatting 내역 모델
public class ChatHistory {

	@Id
	private String historyId;

	private Long roomId;

	private Long senderId;

	private String senderName;

	private String contentType;

	private String content;

	private LocalDateTime sendDate;

	@Builder
	public ChatHistory(Long roomId, Long senderId, String senderName, String contentType, String content,
		LocalDateTime sendDate) {
		this.roomId = roomId;
		this.senderId = senderId;
		this.senderName = senderName;
		this.contentType = contentType;
		this.content = content;
		this.sendDate = sendDate;

	}

}