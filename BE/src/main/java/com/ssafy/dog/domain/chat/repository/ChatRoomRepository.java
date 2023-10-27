package com.ssafy.dog.domain.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.dog.domain.chat.entity.ChatRoom;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
}


