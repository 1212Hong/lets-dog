package com.ssafy.dog.domain.chat.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.ssafy.dog.domain.chat.entity.mongo.ChatHistory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageDto implements Serializable {

	@NotNull
	private Long roomId;

	@NotNull
	private String contentType;

	@NotNull
	private String content;

	private String senderName;

	private Long senderId;

	private String sendTime;

	private List<Long> readList;

	public void setSendTimeAndSenderAndRead(LocalDateTime sendTime, Long senderId, String senderName,
		List<Long> readList) {
		this.senderName = senderName;
		this.sendTime = sendTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd-hh-mm-ss-a"));
		this.senderId = senderId;
		this.readList = readList;
	}

	public ChatHistory convertEntity(LocalDateTime origTime) {
		return ChatHistory.builder()
			.senderName(senderName)
			.senderId(senderId)
			.roomId(roomId)
			.contentType(contentType)
			.content(content)
			.sendDate(origTime)
			.build();
	}
}
