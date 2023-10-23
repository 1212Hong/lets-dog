package com.ssafy.dog.domain.user.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.dog.domain.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

	User findByUserLoginId(String loginId);

	User findByUserNickname(String nickname);

	List<User> findAll();

}
