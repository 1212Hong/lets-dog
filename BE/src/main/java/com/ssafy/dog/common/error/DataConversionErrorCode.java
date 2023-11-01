package com.ssafy.dog.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DataConversionErrorCode implements ErrorCodeIfs {
	// com.dog.ssafy.dog.utils.StringListConverter.java
	UNABLE_TO_CONVERT_LIST_TO_STRING(500, 1501, "List 를 String 으로 변환할 수 없습니다."),
	UNABLE_TO_CONVERT_STRING_TO_LIST(500, 1502, "String 을 List 로 변환할 수 없습니다."),

	// com.dog.ssafy.dog.domain.dog.model.DogDispositionListConverter.java
	INVALID_DISPOSITION_KEY(500, 1503, "성향을 잘못 입력하였습니다.");

	private final Integer httpStatusCode;
	private final Integer errorCode;
	private final String description;
}
