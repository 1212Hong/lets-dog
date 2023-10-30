package com.ssafy.dog.domain.user.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.dog.common.api.Api;
import com.ssafy.dog.common.error.UserErrorCode;
import com.ssafy.dog.common.exception.ApiException;
import com.ssafy.dog.domain.user.dto.UserDto;
import com.ssafy.dog.domain.user.entity.User;
import com.ssafy.dog.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

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
	public Api<?> create(UserDto userDto) {
		if (userRepository.findByUserLoginId(userDto.getUserLoginId()).isPresent()) {
			throw new ApiException(UserErrorCode.EMAIL_EXISTS);
		}

		if (userRepository.findByUserNickname(userDto.getUserNickname()).isPresent()) {
			throw new ApiException(UserErrorCode.NICKNAME_EXISTS);
		}

		if (!userDto.getUserTermsAgreed().equals(true)) {
			throw new ApiException(UserErrorCode.TERMS_NOT_AGREED);
		}

		if (userRepository.findByUserPhone(userDto.getUserPhone()).isPresent()) {
			throw new ApiException(UserErrorCode.PHONE_EXISTS);
		}

		userDto.setUserPw(passwordEncoder.encode(userDto.getUserPw()));
		User user = userRepository.save(userDto.toEntity());
		return Api.ok(userDto.getUserLoginId() + " 회원가입 성공");
	}
}
