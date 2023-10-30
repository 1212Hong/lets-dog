package com.ssafy.dog.domain.user.service;

import java.util.Optional;

import com.ssafy.dog.common.api.Api;
import com.ssafy.dog.domain.user.dto.UserDto;
import com.ssafy.dog.domain.user.dto.UserLoginDto;
import com.ssafy.dog.domain.user.entity.User;

public interface UserService {

	Optional<User> findByUserLoginId(String userLoginId);

	Optional<User> findByUserNickname(String userNickname);

	Optional<User> findByUserPhone(String userPhone);

	Api<?> create(UserDto userDto);

	Api<?> login(UserLoginDto userLoginDto);
}
