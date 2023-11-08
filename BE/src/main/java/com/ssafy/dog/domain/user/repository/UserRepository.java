package com.ssafy.dog.domain.user.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.dog.domain.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByUserLoginId(String loginId);

	Optional<User> findByUserNickname(String nickname);

	Optional<User> findByUserPhone(String phone);

	List<User> findAll();

	Optional<User> findByUserId(Long userId);

	Optional<User> findUserByUserNickname(String userNickname);

	List<User> findAllByUserAddressContains(String address);

	List<User> findAllByUserAddressContainsAndUserIdIsNot(String address, Long userId);

	// @EntityGraph(attributePaths = "authorities")
	// Optional<User> findOneWithAuthoritiesByUsername(String username);
}
