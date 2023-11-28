package com.ssafy.dog.domain.user.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ssafy.dog.domain.user.entity.Friendship;
import com.ssafy.dog.domain.user.entity.User;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
	Optional<Friendship> findByUserAndFriend(User user, User friend);

	List<Friendship> findAllByUserUserId(Long userId);

	@Query("SELECT f.friend.userId FROM Friendship f WHERE f.user.userId = :userId")
	List<Integer> findFriendIdsByUserId(@Param("userId") Long userId);

}
