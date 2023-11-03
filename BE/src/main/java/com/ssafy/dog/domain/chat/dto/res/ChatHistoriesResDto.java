package com.ssafy.dog.domain.chat.dto.res;

import java.time.LocalDateTime;
import java.util.List;

import com.ssafy.dog.domain.chat.entity.mongo.ChatHistory;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class ChatHistoriesResDto {
	private String historyId;

	private Long roomId;

	private Long senderId;

	private String senderName;

	private String contentType;

	private String content;

	private LocalDateTime sendDate;

	private long readCount;

	private List<Long> readList;

	public ChatHistoriesResDto(ChatHistory history, List<Long> readList) {
		this.historyId = history.getHistoryId();
		this.roomId = history.getRoomId();
		this.senderId = history.getSenderId();
		this.senderName = history.getSenderName();
		this.contentType = history.getContentType();
		this.content = history.getContent();
		this.sendDate = history.getSendDate();
		this.readCount = history.getReadCount();
		this.readList = readList;

	}
}
