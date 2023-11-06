package com.ssafy.dog.domain.dog.model;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class DogDispositionListConverter implements AttributeConverter<List<DogDisposition>, String> {

	private static final String SPLIT_CHAR = "\\|";

	@Override
	public String convertToDatabaseColumn(List<DogDisposition> dispositionList) {
		return dispositionList.stream()
			.map(DogDisposition::name)
			.collect(Collectors.joining("|"));
	}

	@Override
	public List<DogDisposition> convertToEntityAttribute(String dispositionString) {
		return Arrays.stream(dispositionString.split(SPLIT_CHAR))
			.map(key -> {
				try {
					// 공백 제거 및 대문자를 변환하여 문자열로부터 Enum 상수를 반환.
					return DogDisposition.valueOf(key.trim().toUpperCase());
				} catch (IllegalArgumentException e) {
					throw new IllegalArgumentException(e);
				}
			})
			.collect(Collectors.toList());
	}
}
