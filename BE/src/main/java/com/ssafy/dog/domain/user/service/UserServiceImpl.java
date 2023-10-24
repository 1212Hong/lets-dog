package com.ssafy.dog.domain.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.dog.domain.user.entity.User;
import com.ssafy.dog.domain.user.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service("UserService")
@Slf4j
public class UserServiceImpl implements UserService {
	@Autowired
	private UserRepository userRepository;

	// @Autowired
	// private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	public User findByUserLoginId(String loginId) {
		return userRepository.findByUserLoginId(loginId);
	}

	@Override
	public User findByUserNickname(String nickname) {
		return userRepository.findByUserNickname(nickname);
	}
}
