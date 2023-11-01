package com.ssafy.dog.domain.chat.repository.mongo;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.ssafy.dog.domain.chat.entity.mongo.ChatHistory;

@Repository
public interface ChatHistoryRepository extends MongoRepository<ChatHistory, String> {

	Optional<ChatHistory> findByRoomId(Long roomId);
}
