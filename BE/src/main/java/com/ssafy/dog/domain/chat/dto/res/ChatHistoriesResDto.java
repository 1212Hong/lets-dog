package com.ssafy.dog.domain.chat.dto.res;

import java.time.format.DateTimeFormatter;
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

	private String sendDate;

	private List<Long> readList;

	public ChatHistoriesResDto(ChatHistory history, List<Long> readList) {
		this.historyId = history.getHistoryId();
		this.roomId = history.getRoomId();
		this.senderId = history.getSenderId();
		this.senderName = history.getSenderName();
		this.contentType = history.getContentType();
		this.content = history.getContent();
		this.sendDate = history.getSendDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-hh-mm-ss-a"));
		this.readList = readList;

	}
}
