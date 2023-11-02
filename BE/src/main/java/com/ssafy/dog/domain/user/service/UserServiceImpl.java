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
import com.ssafy.dog.domain.user.dto.request.UserLoginRequestDto;
import com.ssafy.dog.domain.user.dto.request.UserSignupRequestDto;
import com.ssafy.dog.domain.user.dto.response.UserLoginResponseDto;
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
	public Api<String> create(UserSignupRequestDto userSignupRequestDto) {

		if (!isValidEmail(userSignupRequestDto.getUserLoginId())) {
			throw new ApiException(UserErrorCode.INVALID_EMAIL);
		}

		if (!isValidPassword(userSignupRequestDto.getUserPw1())) {
			throw new ApiException(UserErrorCode.INVALID_PASSWORD);
		}

		if (userRepository.findByUserLoginId(userSignupRequestDto.getUserLoginId()).isPresent()) {
			throw new ApiException(UserErrorCode.EMAIL_EXISTS);
		}

		if (userRepository.findByUserNickname(userSignupRequestDto.getUserNickname()).isPresent()) {
			throw new ApiException(UserErrorCode.NICKNAME_EXISTS);
		}

		if (!userSignupRequestDto.getUserTermsAgreed().equals(true)) {
			throw new ApiException(UserErrorCode.TERMS_NOT_AGREED);
		}

		if (userRepository.findByUserPhone(userSignupRequestDto.getUserPhone()).isPresent()) {
			throw new ApiException(UserErrorCode.PHONE_EXISTS);
		}

		String encodedPassword = passwordEncoder.encode(userSignupRequestDto.getUserPw1());
		User user = User.UserBuilder.anUser()
			.withUserLoginId(userSignupRequestDto.getUserLoginId())
			.withUserPw(encodedPassword)
			.withUserNickname(userSignupRequestDto.getUserNickname())
			.withUserPhone(userSignupRequestDto.getUserPhone())
			.withUserTermsAgreed(userSignupRequestDto.getUserTermsAgreed())
			.withUserIsRemoved(false)
			.withUserRole(UserRole.USER)
			.build();

		userRepository.save(user);

		return Api.ok(userSignupRequestDto.getUserLoginId() + " 회원가입 성공");
	}

	@Transactional
	@Override
	public Api<UserLoginResponseDto> login(UserLoginRequestDto userLoginRequestDto) {

		if (!isValidEmail(userLoginRequestDto.getUserLoginId())) {
			throw new ApiException(UserErrorCode.INVALID_EMAIL);
		}

		if (!isValidPassword(userLoginRequestDto.getUserPw())) {
			throw new ApiException(UserErrorCode.INVALID_PASSWORD);
		}

		User user = userRepository.findByUserLoginId(userLoginRequestDto.getUserLoginId())
			.orElseThrow(() -> new ApiException(UserErrorCode.USER_NOT_FOUND));

		if (!passwordEncoder.matches(userLoginRequestDto.getUserPw(), user.getPassword())) {
			throw new ApiException(UserErrorCode.WRONG_PASSWORD);
		}

		// Authentication 객체 생성
		GrantedAuthority authority = new SimpleGrantedAuthority(user.getUserRole().getValue());
		List<GrantedAuthority> authorities = Collections.singletonList(authority);

		Authentication auth = new UsernamePasswordAuthenticationToken(
			user.getUserLoginId(), user.getPassword(), authorities
		);

		JwtToken jwtToken = jwtTokenProvider.generateToken(auth);

		return Api.ok(new UserLoginResponseDto(user.getUserLoginId(), user.getUserNickname(), user.getUserPicture(),
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
