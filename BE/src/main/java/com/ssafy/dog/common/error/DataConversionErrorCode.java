package com.ssafy.dog.common.error;

import lombok.Getter;

@Getter
public enum DataConversionErrorCode implements ErrorCodeIfs {
	// com.dog.ssafy.dog.domain.dog.model.DogDispositionListConverter.java
	INVALID_DISPOSITION_KEY(500, 1501, "성향을 잘못 입력하였습니다.");

	private final Integer httpStatusCode;
	private final Integer errorCode;
	private final String description;

	DataConversionErrorCode(Integer httpStatusCode, Integer errorCode, String description) {
		this.httpStatusCode = httpStatusCode;
		this.errorCode = errorCode;
		this.description = description;
	}
}
