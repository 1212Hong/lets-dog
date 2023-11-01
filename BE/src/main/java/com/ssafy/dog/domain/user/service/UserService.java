package com.ssafy.dog.domain.user.service;

import com.ssafy.dog.common.api.Api;
import com.ssafy.dog.domain.user.dto.UserLoginDto;
import com.ssafy.dog.domain.user.dto.UserSignupDto;

public interface UserService {

	Api<?> create(UserSignupDto userSignupDto);

	Api<?> login(UserLoginDto userLoginDto);
}
