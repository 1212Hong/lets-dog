package com.dog.fileupload.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

/*
사용 예시 필요시 에러코드 생성해 사용
유저 서비스 에러는 1000번대
 */
@AllArgsConstructor
@Getter
public enum UserErrorCode implements ErrorCodeIfs {

	USER_NOT_FOUND(400, 1404, "사용자를 찾을 수 없음.");

	private final Integer httpStatusCode;
	private final Integer errorCode;
	private final String description;

}
