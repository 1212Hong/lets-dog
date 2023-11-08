package com.ssafy.dog.domain.user.repository;

import com.ssafy.dog.domain.user.entity.Friendship;
import com.ssafy.dog.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
    Optional<Friendship> findByUserAndFriend(User user, User friend);

    List<Friendship> findAllByUserUserId(Long userId);
}
