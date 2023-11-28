package com.ssafy.dog.domain.chat.entity.mongo;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(collection = "chat_read")
// MongoDB Chatting 내역 읽은 사람
public class ChatRead {

	@Id
	private String readId;

	private Long roomId;

	private String historyId;

	private List<Long> readList;

	@Builder
	public ChatRead(Long roomId, String historyId, List<Long> readList) {
		this.roomId = roomId;
		this.historyId = historyId;
		this.readList = readList;
	}
}