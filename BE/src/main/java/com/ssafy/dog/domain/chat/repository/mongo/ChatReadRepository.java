package com.ssafy.dog.domain.chat.repository.mongo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.ssafy.dog.domain.chat.entity.mongo.ChatRead;

@Repository
public interface ChatReadRepository extends MongoRepository<ChatRead, String> {

	Optional<ChatRead> findByHistoryId(String historyId);

	List<ChatRead> findByRoomId(Long roomId);
}
