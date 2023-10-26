package com.ssafy.dog.domain.user.service;

import java.util.Optional;

import com.ssafy.dog.domain.user.dto.UserDto;
import com.ssafy.dog.domain.user.entity.User;

public interface UserService {

	Optional<User> findByUserLoginId(String userLoginId);

	Optional<User> findByUserNickname(String userNickname);

	public Long signUp(UserDto userDto) throws Exception;
}
