package com.ssafy.dog.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

/*
사용 예시 필요시 에러코드 생성해 사용
유저 서비스 에러는 1000번대
 */
@AllArgsConstructor
@Getter
public enum ChatErrorCode implements ErrorCodeIfs {

	JWT_NOT_FOUND(401, 1404, "JWT 토큰이 없습니다");

	private final Integer httpStatusCode;
	private final Integer errorCode;
	private final String description;

}
