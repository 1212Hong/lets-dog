package com.ssafy.dog.domain.chat.repository.redis;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ssafy.dog.domain.chat.entity.redis.ChatRoomUsers;

@Repository
public interface ChatRoomUsersRepository extends CrudRepository<ChatRoomUsers, Long> {

	List<ChatRoomUsers> findByChatRoomId(Long chatRoomNo);

	Optional<ChatRoomUsers> findByChatRoomIdAndUserId(Long chatRoomNo, Long userId);
}
