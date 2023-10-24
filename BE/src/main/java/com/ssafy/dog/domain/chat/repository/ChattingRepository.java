package com.ssafy.dog.domain.chat.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.ssafy.dog.domain.chat.entity.Chatting;

@Repository
public interface ChattingRepository extends MongoRepository<Chatting, String> {

	Optional<Chatting> findByRoomId(String chatRoomNo);
}
