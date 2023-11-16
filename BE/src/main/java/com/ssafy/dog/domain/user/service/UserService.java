package com.ssafy.dog.domain.user.service;

import com.ssafy.dog.common.api.Api;
import com.ssafy.dog.domain.user.dto.request.UserLoginReq;
import com.ssafy.dog.domain.user.dto.request.UserSignupReq;
import com.ssafy.dog.domain.user.dto.request.UserUpdateReq;
import com.ssafy.dog.domain.user.dto.response.IsDuplicatedRes;
import com.ssafy.dog.domain.user.dto.response.UserLoginRes;
import com.ssafy.dog.domain.user.dto.response.UserReadRes;
import com.ssafy.dog.domain.user.dto.response.UserUpdateRes;
import org.springframework.validation.Errors;

import javax.validation.Valid;
import java.util.Map;

public interface UserService {

    Api<Object> create(@Valid UserSignupReq userSignupReq, Errors errors);

    Api<UserLoginRes> login(UserLoginReq userLoginReq);

    Api<UserUpdateRes> updateByUserNickname(String nickname, @Valid UserUpdateReq userUpdateReq);

    Api<UserReadRes> getByUserNickname(String userNickname);

    Map<String, String> validateHandling(Errors errors);

    Api<IsDuplicatedRes> isDuplicatedEmail(String email);

    Api<IsDuplicatedRes> isDuplicatedNickname(String nickname);

    boolean isValidEmail(String email);

    boolean isValidPassword(String password);

    boolean isValidPhoneNumber(String phoneNumber);
}
