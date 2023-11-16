package com.ssafy.dog.domain.user.controller;

import com.ssafy.dog.common.api.Api;
import com.ssafy.dog.domain.user.dto.request.UserLoginReq;
import com.ssafy.dog.domain.user.dto.request.UserSignupReq;
import com.ssafy.dog.domain.user.dto.request.UserUpdateReq;
import com.ssafy.dog.domain.user.dto.response.IsDuplicatedRes;
import com.ssafy.dog.domain.user.service.UserService;
import com.ssafy.dog.util.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequestMapping("/api/user")
@RequiredArgsConstructor
@RestController
@Slf4j
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    @Operation(summary = "회원가입")
    public Api<Object> signUp(@Valid @RequestBody UserSignupReq userSignupReq, Errors errors) {

        return userService.create(userSignupReq, errors);
    }

    @PostMapping("/login")
    @Operation(summary = "로그인")
    public Api<?> login(@Valid @RequestBody UserLoginReq userLoginReq, Errors errors) {

        return userService.login(userLoginReq);
    }

    @PatchMapping("/update")
    @Operation(summary = "유저 업데이트 (우선 전달 안 한 값은 null로 들어감)")
    Api<?> update(@Valid @RequestBody UserUpdateReq userUpdateReq) {
        String nickname = SecurityUtils.getUser().getUserNickname();

        log.info("nickname : {}", nickname);

        return userService.updateByUserNickname(nickname, userUpdateReq);
    }

    @GetMapping("/get/{nickname}")
    @Operation(summary = "유저 정보 조회")
    Api<?> read(@PathVariable("nickname") String userNickname) {

        return userService.getByUserNickname(userNickname);
    }

    @GetMapping("/duplicate-nickname/{nickname}")
    @Operation(summary = "닉네임 중복 조회")
    Api<IsDuplicatedRes> duplicateCheckNickname(@PathVariable("nickname") String nickname) {
        return userService.isDuplicatedNickname(nickname);
    }
}
