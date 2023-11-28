package com.ssafy.dog.domain.chat.dto.res;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ChatListResDto {
	private Long roomId;
	private String roomTitle;
	private Map<String, String> roomMembers;
}
