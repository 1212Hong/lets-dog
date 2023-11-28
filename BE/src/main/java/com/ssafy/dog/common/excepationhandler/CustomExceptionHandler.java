package com.ssafy.dog.common.excepationhandler;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.ssafy.dog.common.api.Api;
import com.ssafy.dog.common.error.ErrorCode;
import com.ssafy.dog.common.exception.ApiException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
@Order(value = Integer.MIN_VALUE) //최우선처리
public class CustomExceptionHandler {

	@ExceptionHandler(value = ApiException.class)
	public ResponseEntity<Api<Object>> apiException(ApiException apiException) {

		log.error("", apiException);

		var errorCode = apiException.getErrorCodeIfs();

		return ResponseEntity
			.status(errorCode.getHttpStatusCode())
			.body(
				Api.error(errorCode, apiException.getErrorDescription())
			);
	}

	@ExceptionHandler(value = AccessDeniedException.class)
	public ResponseEntity<Api<Object>> handleForbidden(AccessDeniedException e) {
		return ResponseEntity
			.status(HttpStatus.FORBIDDEN)
			.body(Api.error(ErrorCode.FORBIDDEN, e.getMessage()));
	}

	@ExceptionHandler(value = JsonMappingException.class)
	public ResponseEntity<Api<Object>> jsonMappingException(ApiException apiException) {

		log.error("", apiException);

		var errorCode = apiException.getErrorCodeIfs();

		return ResponseEntity
			.status(errorCode.getHttpStatusCode())
			.body(
				Api.error(errorCode, apiException.getErrorDescription())
			);
	}

}
