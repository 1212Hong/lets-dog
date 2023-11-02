package com.ssafy.dog.domain.chat.dto;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
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

	private String id;

	@NotNull
	private Long roomId;

	@NotNull
	private String contentType;

	@NotNull
	private String content;

	private String senderName;

	private Long senderId;

	private long sendTime;

	private Integer readCount;

	private List<Long> readList;

	public void setSendTimeAndSenderAndRead(LocalDateTime sendTime, Long senderId, String senderName, Integer readCount,
		List<Long> readList) {
		this.senderName = senderName;
		this.sendTime = sendTime.atZone(ZoneId.of("Asia/Seoul")).toInstant().toEpochMilli();
		this.senderId = senderId;
		this.readCount = readCount;
		this.readList = readList;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ChatHistory convertEntity() {
		return ChatHistory.builder()
			.senderName(senderName)
			.senderId(senderId)
			.roomId(roomId)
			.contentType(contentType)
			.content(content)
			.sendDate(Instant.ofEpochMilli(sendTime).atZone(ZoneId.of("Asia/Seoul")).toLocalDateTime())
			.readCount(readCount)
			.build();
	}
}
