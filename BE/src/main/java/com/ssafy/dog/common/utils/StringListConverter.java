package com.ssafy.dog.common.utils;

import java.io.IOException;
import java.util.List;

import javax.persistence.AttributeConverter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.dog.common.error.DataConversionErrorCode;
import com.ssafy.dog.common.exception.ApiException;

import lombok.extern.slf4j.Slf4j;

// 코드 출처 : https://green-bin.tistory.com/106
// 사용 안하는 파일, 추후에도 사용 안하면 삭제 예정
@Slf4j
public class StringListConverter implements AttributeConverter<List<String>, String> {

	private static final ObjectMapper mapper = new ObjectMapper()
		.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false)
		.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);

	@Override
	public String convertToDatabaseColumn(List<String> attribute) {
		try {
			return mapper.writeValueAsString(attribute);
		} catch (JsonProcessingException e) {
			log.debug("StringListConverter.convertToDatabaseColumn exception occur attribute: {}",
				attribute.toString());
			throw new ApiException(DataConversionErrorCode.UNABLE_TO_CONVERT_LIST_TO_STRING);
		}
	}

	@Override
	public List<String> convertToEntityAttribute(String dbData) {
		try {
			return mapper.readValue(dbData, List.class);
		} catch (IOException e) {
			log.debug("StringListConverter.convertToEntityAttribute exception occur dbData: {}", dbData);
			throw new ApiException(DataConversionErrorCode.UNABLE_TO_CONVERT_STRING_TO_LIST);
		}
	}
}
