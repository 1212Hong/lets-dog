package com.ssafy.dog.domain.user.service;

import com.ssafy.dog.common.api.Api;
import com.ssafy.dog.common.error.UserErrorCode;
import com.ssafy.dog.common.exception.ApiException;
import com.ssafy.dog.domain.user.dto.request.UserLoginReq;
import com.ssafy.dog.domain.user.dto.request.UserSignupReq;
import com.ssafy.dog.domain.user.dto.request.UserUpdateReq;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
            "^(?=.*[A-Z])(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,16}$");
    private static final Pattern PHONE_NUMBER_PATTERN = Pattern.compile("^\\d{10,11}$");

    @Transactional
    @Override
    public Api<String> create(@Valid UserSignupReq userSignupReq) {

        String encodedPassword = passwordEncoder.encode(userSignupReq.getUserPw1());
        User user = User.UserBuilder.anUser()
                .withUserLoginId(userSignupReq.getUserLoginId())
                .withUserPw(encodedPassword)
                .withUserNickname(userSignupReq.getUserNickname())
                .withUserPhone(userSignupReq.getUserPhone())
                .withUserTermsAgreed(userSignupReq.getUserTermsAgreed())
                .withUserIsRemoved(false)
                .withUserRole(UserRole.USER)
                .withUserPicture("https://mblogthumb-phinf.pstatic.net/MjAyMDA2MTBfMTY1/MDAxNTkxNzQ2ODcyOTI2.Yw5WjjU3IuItPtqbegrIBJr3TSDMd_OPhQ2Nw-0-0ksg.8WgVjtB0fy0RCv0XhhUOOWt90Kz_394Zzb6xPjG6I8gg.PNG.lamute/user.png?type=w800")
                .build();

        userRepository.save(user);

        return Api.ok(userSignupReq.getUserLoginId() + " 회원가입 성공");
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
