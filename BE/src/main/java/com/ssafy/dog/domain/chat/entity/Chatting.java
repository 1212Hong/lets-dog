package com.ssafy.dog.domain.chat.entity;

import java.time.LocalDateTime;

import javax.persistence.Id;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(collection = "chatting")
// MongoDB Chatting 모델
public class Chatting {

	@Id
	private String id;
	private Long roomId;
	private Long senderId;
	private String senderName;
	private String contentType;
	private String content;
	private LocalDateTime sendDate;
	private long readCount;

}