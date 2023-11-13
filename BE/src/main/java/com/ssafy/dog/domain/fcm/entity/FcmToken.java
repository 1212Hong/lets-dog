package com.ssafy.dog.domain.fcm.entity;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.Builder;
import lombok.Getter;

@Getter
@RedisHash(value = "fmc_token")
public class FcmToken implements Serializable {

	@Id
	private Long userId;

	private String fcmToken;

	@Builder
	public FcmToken(Long userId, String fcmToken) {
		this.userId = userId;
		this.fcmToken = fcmToken;
	}
}
