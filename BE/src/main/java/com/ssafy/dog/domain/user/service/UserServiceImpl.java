package com.ssafy.dog.domain.user.service;

import com.ssafy.dog.common.api.Api;
import com.ssafy.dog.common.error.UserErrorCode;
import com.ssafy.dog.common.exception.ApiException;
import com.ssafy.dog.domain.fcm.entity.FcmToken;
import com.ssafy.dog.domain.fcm.repository.FcmTokenRepository;
import com.ssafy.dog.domain.user.dto.request.UserLoginReq;
import com.ssafy.dog.domain.user.dto.request.UserSignupReq;
import com.ssafy.dog.domain.user.dto.request.UserUpdateReq;
import com.ssafy.dog.domain.user.dto.response.IsDuplicatedRes;
import com.ssafy.dog.domain.user.dto.response.UserLoginRes;
import com.ssafy.dog.domain.user.dto.response.UserReadRes;
import com.ssafy.dog.domain.user.dto.response.UserUpdateRes;
import com.ssafy.dog.domain.user.entity.User;
import com.ssafy.dog.domain.user.model.UserRole;
import com.ssafy.dog.domain.user.repository.UserRepository;
import com.ssafy.dog.security.JwtToken;
import com.ssafy.dog.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import javax.validation.Valid;
import java.util.*;
import java.util.regex.Pattern;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final FcmTokenRepository fcmTokenRepository;

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
            "^(?=.*[A-Z])(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,16}$");
    private static final Pattern PHONE_NUMBER_PATTERN = Pattern.compile("^\\d{10,11}$");

    @Transactional
    @Override
    public Api<Object> create(@Valid UserSignupReq userSignupReq, Errors errors) {

        if (!isValidEmail(userSignupReq.getUserLoginId())) {
            throw new ApiException(UserErrorCode.INVALID_EMAIL);
        }

        if (!isValidPassword(userSignupReq.getUserPw1())) {
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

        if (!userSignupReq.getUserPw1().equals(userSignupReq.getUserPw2())) {
            throw new ApiException(UserErrorCode.NOT_SAME_PASSWORDS);
        }

        if (errors.hasErrors()) {
            // 유효성 통과 못한 필드와 메시지를 핸들링
            Map<String, String> validatorResult = validateHandling(errors);

            // 각 유효성 실패에 대한 로그 출력
            validatorResult.forEach((field, message) -> log.error("INVALID_SIGNUP_FIELD: {}, {}", field, message));

            // 유효성 검사 실패 응답 리턴
            return Api.error(UserErrorCode.INVALID_SIGNUP_FIELDS);
        }

        String encodedPassword = passwordEncoder.encode(userSignupReq.getUserPw1());
        User user = User.UserBuilder.anUser()
                .withUserLoginId(userSignupReq.getUserLoginId())
                .withUserPw(encodedPassword)
                .withUserNickname(userSignupReq.getUserNickname())
                .withUserTermsAgreed(userSignupReq.getUserTermsAgreed())
                .withUserIsRemoved(false)
                .withUserRole(UserRole.USER)
                .withUserPicture(
                        "http://k9c205.p.ssafy.io:9000/api/file/e19559b6-6372-4eee-aac8-40c04e3755fa.png_encoded.jpg")
                .build();

        userRepository.save(user);

        Map<String, String> ret = new HashMap<>();
        ret.put("userLoginId", user.getUserLoginId());

        return Api.ok(ret);
    }

    @Transactional
    @Override
    public Api<UserLoginRes> login(@Valid UserLoginReq userLoginReq) {

        if (!isValidEmail(userLoginReq.getUserLoginId())) {
            throw new ApiException(UserErrorCode.INVALID_EMAIL);
        }

        if (!isValidPassword(userLoginReq.getUserPw())) {
            throw new ApiException(UserErrorCode.INVALID_PASSWORD);
        }

        User user = userRepository.findByUserLoginId(userLoginReq.getUserLoginId())
                .orElseThrow(() -> new ApiException(UserErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(userLoginReq.getUserPw(), user.getPassword())) {
            throw new ApiException(UserErrorCode.WRONG_PASSWORD);
        }

        // Authentication 객체 생성
        GrantedAuthority authority = new SimpleGrantedAuthority(user.getUserRole().getValue());
        List<GrantedAuthority> authorities = Collections.singletonList(authority);

        Authentication auth = new UsernamePasswordAuthenticationToken(
                user.getUserLoginId(), user.getPassword(), authorities
        );

        JwtToken jwtToken = jwtTokenProvider.generateToken(auth);

        // Redis에 userId, fcmToken 저장
        fcmTokenRepository.save(new FcmToken(user.getUserId(), userLoginReq.getFcmToken()));

        return Api.ok(new UserLoginRes(user.getUserLoginId(), user.getUserNickname(), user.getUserPicture(),
                jwtToken.getAccessToken()));

        // return Api.ok(jwtToken.getAccessToken()); // UserLoginResponseDto 로 보내기
    }

    @Transactional
    @Override
    public Api<UserUpdateRes> updateByUserNickname(String userNickname, UserUpdateReq userUpdateReq) {
        User user = userRepository.findByUserNickname(userNickname)
                .orElseThrow(() -> new ApiException(UserErrorCode.USER_NOT_FOUND));

        user.updateUser(
                userUpdateReq.getUserNickname(),
                userUpdateReq.getUserPhone(),
                userUpdateReq.getUserPicture(),
                userUpdateReq.getUserAboutMe(),
                userUpdateReq.getUserGender(),
                userUpdateReq.getUserLatitude(),
                userUpdateReq.getUserLatitude(),
                userUpdateReq.getUserAddress()
        );

        return Api.ok(user.toUserUpdateRes());
    }

    @Transactional
    @Override
    public Api<UserReadRes> getByUserNickname(String userNickname) {
        User user = userRepository.findUserByUserNickname(userNickname)
                .orElseThrow(() -> new ApiException(UserErrorCode.USER_NOT_FOUND));

        return Api.ok(user.toUserReadRes());
    }

    @Transactional
    @Override
    public Api<IsDuplicatedRes> isDuplicatedEmail(String email) {
        Optional<User> user = userRepository.findByUserLoginId(email);
        IsDuplicatedRes res = new IsDuplicatedRes();

        if (user.isEmpty()) {
            res.setIsDuplicated(false);
        } else {
            res.setIsDuplicated(true);
        }

        return Api.ok(res);
    }

    @Transactional
    @Override
    public Api<IsDuplicatedRes> isDuplicatedNickname(String nickname) {
        Optional<User> user = userRepository.findByUserNickname(nickname);
        IsDuplicatedRes res = new IsDuplicatedRes();

        if (user.isEmpty()) {
            res.setIsDuplicated(false);
        } else {
            res.setIsDuplicated(true);
        }

        return Api.ok(res);
    }

    // 회원가입 시, 유효성 체크
    @Transactional(readOnly = true)
    public Map<String, String> validateHandling(Errors errors) {
        Map<String, String> validatorResult = new HashMap<>();

        // 유효성 검사에 실패한 필드 목록을 받음
        for (FieldError error : errors.getFieldErrors()) {
            String validKeyName = String.format("%s", error.getField());
            validatorResult.put(validKeyName, error.getDefaultMessage());
        }

        return validatorResult;
    }

    // 이메일 형식 검증 메소드
    public boolean isValidEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }

    public boolean isValidPassword(String password) {
        return PASSWORD_PATTERN.matcher(password).matches();
    }

    public boolean isValidPhoneNumber(String phoneNumber) {
        return PHONE_NUMBER_PATTERN.matcher(phoneNumber).matches();
    }
}
