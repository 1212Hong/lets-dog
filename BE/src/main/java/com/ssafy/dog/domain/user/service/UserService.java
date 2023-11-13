package com.ssafy.dog.domain.user.service;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.validation.Errors;

import com.ssafy.dog.common.api.Api;
import com.ssafy.dog.domain.user.dto.request.UserLoginReq;
import com.ssafy.dog.domain.user.dto.request.UserSignupReq;
import com.ssafy.dog.domain.user.dto.request.UserUpdateReq;
import com.ssafy.dog.domain.user.dto.response.UserLoginRes;
import com.ssafy.dog.domain.user.dto.response.UserReadRes;
import com.ssafy.dog.domain.user.dto.response.UserUpdateRes;

public interface UserService {

	Api<?> create(@Valid UserSignupReq userSignupReq, Errors errors);

	Api<UserLoginRes> login(UserLoginReq userLoginReq);

	Api<UserUpdateRes> updateByUserNickname(String nickname, @Valid UserUpdateReq userUpdateReq);

	Api<UserReadRes> getByUserNickname(String userNickname);

	Map<String, String> validateHandling(Errors errors);

	boolean isValidEmail(String email);

	boolean isValidPassword(String password);

	boolean isValidPhoneNumber(String phoneNumber);
}
