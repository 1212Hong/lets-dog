package com.ssafy.dog.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

/*
사용 예시 필요시 에러코드 생성해 사용
유저 서비스 에러는 1000번대
 */
@AllArgsConstructor
@Getter
public enum UserErrorCode implements ErrorCodeIfs {
	// 회원가입 관련
	EMAIL_EXISTS(400, 1404, "해당 이메일은 이미 사용 중입니다."),
	INVALID_PASSWORD(400, 1405, "형식에 맞지 않는 비밀번호 입니다."),
	NICKNAME_EXISTS(400, 1406, "해당 닉네임은 이미 사용 중입니다."),
	TERMS_NOT_AGREED(400, 1407, "약관 동의를 하지 않았거나, 해당 값이 null 입니다."),
	PHONE_EXISTS(400, 1408, "이미 가입한 핸드폰 번호입니다."),
	INVALID_EMAIL(400, 1409, "형식에 맞지 않는 이메일 입니다."),

	// 로그인 관련
	USER_NOT_FOUND(400, 1410, "제공된 이메일로 사용자를 찾을 수 없음."),
	WRONG_PASSWORD(400, 1411, "유저는 존재하나 잘못된 비밀번호입니다.");
	// INVALID_PASSWORD(400, 1405, "형식에 맞지 않는 비밀번호 입니다."),
	// INVALID_EMAIL(400, 1409, "형식에 맞지 않는 이메일 입니다."),

	private final Integer httpStatusCode;
	private final Integer errorCode;
	private final String description;

}
