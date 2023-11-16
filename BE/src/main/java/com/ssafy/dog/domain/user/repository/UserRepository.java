package com.ssafy.dog.domain.user.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

	@Query("SELECT u FROM User u WHERE " +
		"(6371 * acos(cos(radians(:latitude)) * cos(radians(u.userLatitude)) * " +
		"cos(radians(u.userLongitude) - radians(:longitude)) + " +
		"sin(radians(:latitude)) * sin(radians(u.userLatitude)))) < :distance " +
		"AND u.userId != :userId")
	List<User> findAllWithinDistance(@Param("latitude") double latitude,
		@Param("longitude") double longitude,
		@Param("distance") double distance,
		@Param("userId") Long userId);

	// @EntityGraph(attributePaths = "authorities")
	// Optional<User> findOneWithAuthoritiesByUsername(String username);
}
