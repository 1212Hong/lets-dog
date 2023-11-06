package com.ssafy.dog.domain.user.service;

import com.ssafy.dog.common.api.Api;
import com.ssafy.dog.domain.user.dto.request.UserLoginReq;
import com.ssafy.dog.domain.user.dto.request.UserSignupReq;
import com.ssafy.dog.domain.user.dto.response.UserLoginRes;

public interface UserService {

	Api<String> create(UserSignupReq userSignupReq);

	Api<UserLoginRes> login(UserLoginReq userLoginReq);
}
