package com.ssafy.dog.domain.dog.model;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.ssafy.dog.common.error.DataConversionErrorCode;
import com.ssafy.dog.common.exception.ApiException;

@Converter(autoApply = true)
public class DogDispositionListConverter implements AttributeConverter<List<DogDisposition>, String> {

	private static final String SPLIT_CHAR = "|";

	@Override
	public String convertToDatabaseColumn(List<DogDisposition> dispositionList) {
		return dispositionList.stream()
			.map(DogDisposition::getKey)
			.collect(Collectors.joining(SPLIT_CHAR));
	}

	@Override
	public List<DogDisposition> convertToEntityAttribute(String dispositionString) {
		return Arrays.stream(dispositionString.split(SPLIT_CHAR))
			.map(key -> Arrays.stream(DogDisposition.values())
				.filter(disp -> disp.getKey().equals(key))
				.findFirst()
				.orElseThrow(() -> new ApiException(DataConversionErrorCode.INVALID_DISPOSITION_KEY)))
			.collect(Collectors.toList());
	}
}
