package com.ssafy.dog.domain.user.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.dog.domain.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

	User save(User user);

	Optional<User> findByUserLoginId(String loginId);

	Optional<User> findByUserNickname(String nickname);

	List<User> findAll();

	// @EntityGraph(attributePaths = "authorities")
	// Optional<User> findOneWithAuthoritiesByUsername(String username);

}
