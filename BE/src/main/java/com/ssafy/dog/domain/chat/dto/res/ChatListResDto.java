package com.ssafy.dog.domain.chat.dto.res;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChatListResDto {
	private Long roomId;
	// private String recentMsg;
	private Map<String, String> roomMembers;
}
