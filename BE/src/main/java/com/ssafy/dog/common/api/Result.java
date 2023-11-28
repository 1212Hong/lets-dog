package com.ssafy.dog.common.api;

import com.ssafy.dog.common.error.ErrorCode;
import com.ssafy.dog.common.error.ErrorCodeIfs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Result {

	private Integer code;
	private String message;
	private String description;

	public static Result ok() {
		return Result.builder()
			.code(ErrorCode.OK.getErrorCode())
			.message(ErrorCode.OK.getDescription())
			.description("성공")
			.build();
	}

	public static Result error(ErrorCodeIfs errorCodeIfs) {
		return Result.builder()
			.code(errorCodeIfs.getErrorCode())
			.message(errorCodeIfs.getDescription())
			.description("에러")
			.build();
	}

	public static Result error(ErrorCodeIfs errorCodeIfs, String description) {
		return Result.builder()
			.code(errorCodeIfs.getErrorCode())
			.message(errorCodeIfs.getDescription())
			.description(description)
			.build();
	}

}


