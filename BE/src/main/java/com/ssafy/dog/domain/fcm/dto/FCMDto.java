package com.ssafy.dog.domain.fcm.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class FCMDto {
	private Long userId;
	private String title;
	private String content;

	@Builder
	public FCMDto(Long userId, String title, String content) {
		this.userId = userId;
		this.title = title;
		this.content = content;
	}

	public static FCMDto createFriendRequest(Long userId, String userNickname) {
		return FCMDto.builder()
			.userId(userId)
			.title("친구 요청")
			.content(userNickname + " 님으로부터의 친구요청")
			.build();
	}

	public static FCMDto acceptFriendRequest(Long userId, String userNickname) {
		return FCMDto.builder()
			.userId(userId)
			.title("친구 요청 수락")
			.content(userNickname + " 님과 친구요청이 수락되었습니다")
			.build();
	}

	public static FCMDto rejectFriendRequest(Long userId, String userNickname) {
		return FCMDto.builder()
			.userId(userId)
			.title("친구 요청 거절")
			.content(userNickname + " 님과 친구요청이 거절되었습니다")
			.build();
	}
}
