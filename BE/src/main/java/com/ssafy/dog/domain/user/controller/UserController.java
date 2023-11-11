package com.ssafy.dog.domain.user.controller;

import com.ssafy.dog.common.api.Api;
import com.ssafy.dog.common.error.UserErrorCode;
import com.ssafy.dog.common.exception.ApiException;
import com.ssafy.dog.domain.user.dto.request.UserLoginReq;
import com.ssafy.dog.domain.user.dto.request.UserSignupReq;
import com.ssafy.dog.domain.user.dto.request.UserUpdateReq;
import com.ssafy.dog.domain.user.entity.User;
import com.ssafy.dog.domain.user.repository.UserRepository;
import com.ssafy.dog.domain.user.service.UserService;
import com.ssafy.dog.util.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

@RequestMapping("/api/user")
@RequiredArgsConstructor
@RestController
@Slf4j
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
            "^(?=.*[A-Z])(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,16}$");
    private static final Pattern PHONE_NUMBER_PATTERN = Pattern.compile("^\\d{10,11}$\n");

    @PostMapping("/signup")
    @Operation(summary = "회원가입")
    public Api<?> signUp(@Valid @RequestBody UserSignupReq userSignupReq, Errors errors) {

        if (!userService.isValidEmail(userSignupReq.getUserLoginId())) {
            throw new ApiException(UserErrorCode.INVALID_EMAIL);
        }

        if (!userService.isValidPassword(userSignupReq.getUserPw1())) {
            throw new ApiException(UserErrorCode.INVALID_PASSWORD);
        }

        if (userRepository.findByUserLoginId(userSignupReq.getUserLoginId()).isPresent()) {
            throw new ApiException(UserErrorCode.EMAIL_EXISTS);
        }

        if (userRepository.findUserByUserNickname(userSignupReq.getUserNickname()).isPresent()) {
            throw new ApiException(UserErrorCode.NICKNAME_EXISTS);
        }

        if (!userSignupReq.getUserTermsAgreed().equals(true)) {
            throw new ApiException(UserErrorCode.TERMS_NOT_AGREED);
        }

        if (userRepository.findByUserPhone(userSignupReq.getUserPhone()).isPresent()) {
            throw new ApiException(UserErrorCode.PHONE_EXISTS);
        }

        if (!userSignupReq.getUserPw1().equals(userSignupReq.getUserPw2())) {
            throw new ApiException(UserErrorCode.NOT_SAME_PASSWORDS);
        }

        if (!userService.isValidPhoneNumber(userSignupReq.getUserPhone())) {
            throw new ApiException(UserErrorCode.INVALID_PHONE_NUMBER);
        }

        if (errors.hasErrors()) {
            // 유효성 통과 못한 필드와 메시지를 핸들링
            Map<String, String> validatorResult = userService.validateHandling(errors);

            // 각 유효성 실패에 대한 로그 출력
            validatorResult.forEach((field, message) -> log.error("INVALID_SIGNUP_FIELD: {}, {}", field, message));

            // 유효성 검사 실패 응답 리턴
            return Api.error(UserErrorCode.INVALID_SIGNUP_FIELDS);
        }

        return userService.create(userSignupReq);
    }

    @PostMapping("/login")
    @Operation(summary = "로그인")
    public Api<?> login(@Valid @RequestBody UserLoginReq userLoginReq, Errors errors) {

        if (!userService.isValidEmail(userLoginReq.getUserLoginId())) {
            throw new ApiException(UserErrorCode.INVALID_EMAIL);
        }

        if (!userService.isValidPassword(userLoginReq.getUserPw())) {
            throw new ApiException(UserErrorCode.INVALID_PASSWORD);
        }

        Optional<User> user = userRepository.findByUserLoginId(userLoginReq.getUserLoginId());

        if (user.isEmpty()) {
            throw new ApiException(UserErrorCode.USER_NOT_FOUND);
        }

        if (!passwordEncoder.matches(userLoginReq.getUserPw(), user.get().getUserPw())) {
            throw new ApiException(UserErrorCode.WRONG_PASSWORD);
        }

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
}
