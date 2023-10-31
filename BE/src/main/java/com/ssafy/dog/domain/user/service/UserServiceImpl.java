package com.ssafy.dog.domain.user.service;

import java.util.Optional;
import java.util.regex.Pattern;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.dog.common.api.Api;
import com.ssafy.dog.common.error.UserErrorCode;
import com.ssafy.dog.common.exception.ApiException;
import com.ssafy.dog.domain.user.dto.UserLoginDto;
import com.ssafy.dog.domain.user.dto.UserSignupDto;
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

	private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");
	private static final Pattern PASSWORD_PATTERN = Pattern.compile(
		"^(?=.*[A-Z])(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,16}$");

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
	public Api<?> create(UserSignupDto userSignupDto) {

		if (!isValidEmail(userSignupDto.getUserLoginId())) {
			throw new ApiException(UserErrorCode.INVALID_EMAIL);
		}

		if (!isValidPassword(userSignupDto.getUserPw1())) {
			throw new ApiException(UserErrorCode.INVALID_PASSWORD);
		}

		if (userRepository.findByUserLoginId(userSignupDto.getUserLoginId()).isPresent()) {
			throw new ApiException(UserErrorCode.EMAIL_EXISTS);
		}

		if (userRepository.findByUserNickname(userSignupDto.getUserNickname()).isPresent()) {
			throw new ApiException(UserErrorCode.NICKNAME_EXISTS);
		}

		if (!userSignupDto.getUserTermsAgreed().equals(true)) {
			throw new ApiException(UserErrorCode.TERMS_NOT_AGREED);
		}

		if (userRepository.findByUserPhone(userSignupDto.getUserPhone()).isPresent()) {
			throw new ApiException(UserErrorCode.PHONE_EXISTS);
		}

		String encodedPassword = passwordEncoder.encode(userSignupDto.getUserPw1());
		User user = User.UserBuilder.anUser()
			.withUserLoginId(userSignupDto.getUserLoginId())
			.withUserPw(encodedPassword)
			.withUserNickname(userSignupDto.getUserNickname())
			.withUserPhone(userSignupDto.getUserPhone())
			.withUserTermsAgreed(userSignupDto.getUserTermsAgreed())
			.withUserIsRemoved(false)
			.build();

		userRepository.save(user);

		return Api.ok(userSignupDto.getUserLoginId() + " 회원가입 성공");
	}

	@Transactional
	@Override
	public Api<?> login(UserLoginDto userLoginDto) {

		if (!isValidEmail(userLoginDto.getUserLoginId())) {
			throw new ApiException(UserErrorCode.INVALID_EMAIL);
		}

		if (!isValidPassword(userLoginDto.getUserPw())) {
			throw new ApiException(UserErrorCode.INVALID_PASSWORD);
		}

		User user = userRepository.findByUserLoginId(userLoginDto.getUserLoginId())
			.orElseThrow(() -> new ApiException(UserErrorCode.USER_NOT_FOUND));

		if (!passwordEncoder.matches(userLoginDto.getUserPw(), user.getPassword())) {
			throw new ApiException(UserErrorCode.WRONG_PASSWORD);
		}

		return Api.ok(jwtTokenProvider.createToken(user.getUserLoginId(), user.getUserRole()));
		// return jwtTokenProvider.createToken(user.getUserLoginId(), user.getUserRoles());
	}

	// 이메일 형식 검증 메소드
	private boolean isValidEmail(String email) {
		return EMAIL_PATTERN.matcher(email).matches();
	}

	private boolean isValidPassword(String password) {
		return PASSWORD_PATTERN.matcher(password).matches();
	}
}
