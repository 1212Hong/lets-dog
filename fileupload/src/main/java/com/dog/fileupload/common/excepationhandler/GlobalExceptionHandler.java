package com.dog.fileupload.common.excepationhandler;


import com.dog.fileupload.common.api.Api;
import com.dog.fileupload.common.error.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
@Order(value = Integer.MAX_VALUE)
public class GlobalExceptionHandler {

	@ExceptionHandler({Exception.class})
	public ResponseEntity<Api<Object>> exception(Exception exception) {
		return ResponseEntity
			.status(500)
			.body(Api.error(ErrorCode.SERVER_ERROR, exception.getMessage()));
	}

}
