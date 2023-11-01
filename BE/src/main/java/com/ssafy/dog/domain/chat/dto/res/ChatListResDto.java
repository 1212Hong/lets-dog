package com.ssafy.dog.domain.chat.dto.res;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChatListResDto {
	private Long roomId;
	// private String recentMsg;
	private List<String> roomMembers;
}
