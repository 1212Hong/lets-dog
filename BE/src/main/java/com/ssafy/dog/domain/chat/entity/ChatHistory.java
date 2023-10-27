package com.ssafy.dog.domain.chat.entity;

import java.time.LocalDateTime;

import javax.persistence.Id;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(collection = "chatting")
// MongoDB Chatting 모델
public class ChatHistory {

	@Id
	private String id;

	private Long roomId;

	private Long senderId;

	private String senderName;

	private String contentType;

	private String content;

	private LocalDateTime sendDate;

	private long readCount;

	@Builder
	public ChatHistory(Long roomId, Long senderId, String senderName, String contentType, String content,
		LocalDateTime sendDate, long readCount) {
		this.roomId = roomId;
		this.senderId = senderId;
		this.senderName = senderName;
		this.contentType = contentType;
		this.content = content;
		this.sendDate = sendDate;
		this.readCount = readCount;

	}

}