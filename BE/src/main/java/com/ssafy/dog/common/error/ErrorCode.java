package com.ssafy.dog.common.error;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode implements ErrorCodeIfs {

	OK(200, 200, "성공"),
	BAD_REQUEST(HttpStatus.BAD_REQUEST.value(), 400, "잘못된 요청"),
	SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), 500, "서버에러"),
	FORBIDDEN(HttpStatus.FORBIDDEN.value(), 403, "Access Denied"),
	NULL_POINT(HttpStatus.INTERNAL_SERVER_ERROR.value(), 512, "Null point"),
	VALIDATION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), 512, "테스트용");

	private final Integer httpStatusCode;
	private final Integer errorCode;
	private final String description;

}
