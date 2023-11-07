package com.ssafy.dog.domain.user.controller;

import com.ssafy.dog.common.api.Api;
import com.ssafy.dog.domain.user.dto.request.UserLoginReq;
import com.ssafy.dog.domain.user.dto.request.UserSignupReq;
import com.ssafy.dog.domain.user.dto.request.UserUpdateReq;
import com.ssafy.dog.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequestMapping("/api/user")
@RequiredArgsConstructor
@RestController
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    @Operation(summary = "회원가입")
    public Api<?> signUp(@Valid @RequestBody UserSignupReq userSignupReq) {
        return userService.create(userSignupReq);
    }

    @PostMapping("/login")
    @Operation(summary = "로그인")
    public Api<?> login(@Valid @RequestBody UserLoginReq userLoginReq) {

        return userService.login(userLoginReq);
    }

    @PatchMapping("/update/{nickname}")
    @Operation(summary = "유저 업데이트 (전달 안 한 속성은 업데이트 안함)")
    Api<?> update(@PathVariable("nickname") String nickname, @Valid @RequestBody UserUpdateReq userUpdateReq) {

        return userService.updateByUserNickname(nickname, userUpdateReq);
    }

    @GetMapping("/get/{nickname}")
    @Operation(summary = "유저 정보 조회")
    Api<?> read(@PathVariable("nickname") String userNickname) {

        return userService.getByUserNickname(userNickname);
    }
}
