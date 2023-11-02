package com.ssafy.dog.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

/*
사용 예시 필요시 에러코드 생성해 사용
유저 서비스 에러는 1000번대
 */
@AllArgsConstructor
@Getter
public enum GpsErrorCode implements ErrorCodeIfs {

	GPS_POINT_NOT_VALID(400, 2404, "위도와 경도를 입력해주세요");

	private final Integer httpStatusCode;
	private final Integer errorCode;
	private final String description;

}
