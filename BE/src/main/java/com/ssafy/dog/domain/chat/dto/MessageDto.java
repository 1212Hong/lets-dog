package com.ssafy.dog.domain.chat.dto;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import javax.validation.constraints.NotNull;

import com.ssafy.dog.domain.chat.entity.Chatting;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

	// @NotNull
	// private Integer saleNo;

	private long sendTime;
	private Integer readCount;
	private String senderEmail;

	public void setSendTimeAndSender(LocalDateTime sendTime, Long senderId, String senderName, Integer readCount) {
		this.senderName = senderName;
		this.sendTime = sendTime.atZone(ZoneId.of("Asia/Seoul")).toInstant().toEpochMilli();
		this.senderId = senderId;
		this.readCount = readCount;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Chatting convertEntity() {
		return Chatting.builder()
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
