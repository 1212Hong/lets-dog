package com.ssafy.dog.domain.chat.repository;

import java.util.List;

import com.ssafy.dog.domain.chat.dto.res.ChatListResDto;

public interface ChatRoomRepositoryCustom {
	List<ChatListResDto> getUserChatRoomsAndUserNicknames(Long userId);

}
