package com.ssafy.dog.domain.user.service;

import com.ssafy.dog.common.api.Api;
import com.ssafy.dog.domain.user.dto.request.UserLoginRequestDto;
import com.ssafy.dog.domain.user.dto.request.UserSignupRequestDto;
import com.ssafy.dog.domain.user.dto.response.UserLoginResponseDto;

public interface UserService {

	Api<String> create(UserSignupRequestDto userSignupRequestDto);

	Api<UserLoginResponseDto> login(UserLoginRequestDto userLoginRequestDto);
}
