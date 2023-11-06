package com.ssafy.dog.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DogErrorCode implements ErrorCodeIfs {
	// Dog Create
	USER_NOT_FOUND(404, 1601, "유저가 존재하지 않습니다."),
	NOT_UNIQUE_ELEMENTS(400, 1602, "dog_disposition_list 의 값들이 겹치지 않아야 합니다.");

	// 프론트와 협의 후 에러 탐지 로직 추가 예정

	private final Integer httpStatusCode;
	private final Integer errorCode;
	private final String description;

}
