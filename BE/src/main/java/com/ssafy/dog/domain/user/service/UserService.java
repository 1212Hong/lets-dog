package com.ssafy.dog.domain.user.service;

import com.ssafy.dog.domain.user.entity.User;

public interface UserService {

	User findByUserLoginId(String userLoginId);

	User findByUserNickname(String userNickname);
}
