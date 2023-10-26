package com.ssafy.dog.domain.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.dog.domain.chat.entity.ChatMessage;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long>, ChatMessageRepositoryCustom {
}


