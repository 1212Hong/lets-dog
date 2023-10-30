package com.ssafy.dog.domain.user.service;

import java.util.Optional;
import java.util.regex.Pattern;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.dog.common.api.Api;
import com.ssafy.dog.common.error.UserErrorCode;
import com.ssafy.dog.common.exception.ApiException;
import com.ssafy.dog.domain.user.dto.UserDto;
import com.ssafy.dog.domain.user.dto.UserLoginDto;
import com.ssafy.dog.domain.user.entity.User;
import com.ssafy.dog.domain.user.repository.UserRepository;
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

	@Override
	public Optional<User> findByUserLoginId(String loginId) {
		log.info("Calling findByUserLoginId with loginId: {}", loginId); // 호출 전 로깅
		Optional<User> result = userRepository.findByUserLoginId(loginId);
		log.info("Result for findByUserLoginId with loginId {}: {}", loginId,
			result.isPresent() ? "Found" : "Not Found"); // 호출 후 로깅
		return result;
	}

	@Override
	public Optional<User> findByUserNickname(String nickname) {
		log.info("Calling findByUserNickname with nickname: {}", nickname); // 호출 전 로깅
		Optional<User> result = userRepository.findByUserNickname(nickname);
		log.info("Result for findByUserNickname with nickname {}: {}", nickname,
			result.isPresent() ? "Found" : "Not Found"); // 호출 후 로깅
		return result;
	}

	public Optional<User> findByUserPhone(String phone) {
		log.info("Calling findByUserPhone with phone: {}", phone); // 호출 전 로깅
		Optional<User> result = userRepository.findByUserPhone(phone);
		log.info("Result for findByUserPhone with phone {}: {}", phone,
			result.isPresent() ? "Found" : "Not Found"); // 호출 후 로깅
		return result;
	}

	@Transactional
	@Override
	public Api<?> create(UserDto dto) {

		if (!isValidEmail(dto.getUserLoginId())) {
			throw new ApiException(UserErrorCode.INVALID_EMAIL);
		}

		if (!isValidPassword(dto.getUserPw())) {
			throw new ApiException(UserErrorCode.INVALID_PASSWORD);
		}

		if (userRepository.findByUserLoginId(dto.getUserLoginId()).isPresent()) {
			throw new ApiException(UserErrorCode.EMAIL_EXISTS);
		}

		if (userRepository.findByUserNickname(dto.getUserNickname()).isPresent()) {
			throw new ApiException(UserErrorCode.NICKNAME_EXISTS);
		}

		if (!dto.getUserTermsAgreed().equals(true)) {
			throw new ApiException(UserErrorCode.TERMS_NOT_AGREED);
		}

		if (userRepository.findByUserPhone(dto.getUserPhone()).isPresent()) {
			throw new ApiException(UserErrorCode.PHONE_EXISTS);
		}

		dto.setUserPw(passwordEncoder.encode(dto.getUserPw()));
		User user = userRepository.save(dto.toEntity());
		return Api.ok(dto.getUserLoginId() + " 회원가입 성공");
	}

	@Transactional
	@Override
	public Api<?> login(UserLoginDto dto) {

		if (!isValidEmail(dto.getUserLoginId())) {
			throw new ApiException(UserErrorCode.INVALID_EMAIL);
		}

		if (!isValidPassword(dto.getUserPw())) {
			throw new ApiException(UserErrorCode.INVALID_PASSWORD);
		}

		User user = userRepository.findByUserLoginId(dto.getUserLoginId())
			.orElseThrow(() -> new ApiException(UserErrorCode.USER_NOT_FOUND));

		if (!passwordEncoder.matches(dto.getUserPw(), user.getPassword())) {
			throw new ApiException(UserErrorCode.WRONG_PASSWORD);
		}

		return Api.ok(jwtTokenProvider.createToken(user.getUserLoginId(), user.getUserRoles()));
		// return jwtTokenProvider.createToken(user.getUserLoginId(), user.getUserRoles());
	}

	// 이메일 형식 검증 메소드
	private boolean isValidEmail(String email) {
		String regex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
		return Pattern.matches(regex, email);
	}

	private boolean isValidPassword(String password) {
		String regex = "^(?=.*[A-Z])(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,16}$";
		return Pattern.matches(regex, password);
	}
}
