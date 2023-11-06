package com.ssafy.dog.domain.user.service;

import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.dog.common.api.Api;
import com.ssafy.dog.common.error.UserErrorCode;
import com.ssafy.dog.common.exception.ApiException;
import com.ssafy.dog.domain.user.dto.request.UserLoginReq;
import com.ssafy.dog.domain.user.dto.request.UserSignupReq;
import com.ssafy.dog.domain.user.dto.response.UserLoginRes;
import com.ssafy.dog.domain.user.entity.User;
import com.ssafy.dog.domain.user.model.UserRole;
import com.ssafy.dog.domain.user.repository.UserRepository;
import com.ssafy.dog.security.JwtToken;
import com.ssafy.dog.security.JwtTokenProvider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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

	@Transactional
	@Override
	public Api<String> create(UserSignupReq userSignupReq) {

		if (!isValidEmail(userSignupReq.getUserLoginId())) {
			throw new ApiException(UserErrorCode.INVALID_EMAIL);
		}

		if (!isValidPassword(userSignupReq.getUserPw1())) {
			throw new ApiException(UserErrorCode.INVALID_PASSWORD);
		}

		if (userRepository.findByUserLoginId(userSignupReq.getUserLoginId()).isPresent()) {
			throw new ApiException(UserErrorCode.EMAIL_EXISTS);
		}

		if (userRepository.findByUserNickname(userSignupReq.getUserNickname()).isPresent()) {
			throw new ApiException(UserErrorCode.NICKNAME_EXISTS);
		}

		if (!userSignupReq.getUserTermsAgreed().equals(true)) {
			throw new ApiException(UserErrorCode.TERMS_NOT_AGREED);
		}

		if (userRepository.findByUserPhone(userSignupReq.getUserPhone()).isPresent()) {
			throw new ApiException(UserErrorCode.PHONE_EXISTS);
		}

		String encodedPassword = passwordEncoder.encode(userSignupReq.getUserPw1());
		User user = User.UserBuilder.anUser()
			.withUserLoginId(userSignupReq.getUserLoginId())
			.withUserPw(encodedPassword)
			.withUserNickname(userSignupReq.getUserNickname())
			.withUserPhone(userSignupReq.getUserPhone())
			.withUserTermsAgreed(userSignupReq.getUserTermsAgreed())
			.withUserIsRemoved(false)
			.withUserRole(UserRole.USER)
			.build();

		userRepository.save(user);

		return Api.ok(userSignupReq.getUserLoginId() + " 회원가입 성공");
	}

	@Transactional
	@Override
	public Api<UserLoginRes> login(UserLoginReq userLoginReq) {

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

	// 이메일 형식 검증 메소드
	private boolean isValidEmail(String email) {
		return EMAIL_PATTERN.matcher(email).matches();
	}

	private boolean isValidPassword(String password) {
		return PASSWORD_PATTERN.matcher(password).matches();
	}
}
