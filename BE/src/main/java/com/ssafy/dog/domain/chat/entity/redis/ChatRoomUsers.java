package com.ssafy.dog.domain.chat.entity.redis;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import lombok.Builder;
import lombok.Getter;

@Getter
@RedisHash(value = "chatRoomUsers")
public class ChatRoomUsers {

	@Id
	private Long id;

	@Indexed
	private Long chatRoomId;

	@Indexed
	private Long userId;
	// private String email;

	@Builder
	public ChatRoomUsers(Long chatRoomId, Long userId) {
		this.chatRoomId = chatRoomId;
		this.userId = userId;
	}
}
