package com.ssafy.dog.domain.chat.repository;

import java.util.List;

import com.ssafy.dog.domain.chat.entity.ChatMessage;

public interface ChatMessageRepositoryCustom {
	List<ChatMessage> getAllMessage();
}
