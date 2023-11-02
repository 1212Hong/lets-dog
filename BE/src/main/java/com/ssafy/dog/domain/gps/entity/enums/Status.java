package com.ssafy.dog.domain.gps.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Status {
	AVAILABLE("이용가능"),
	DELETED("삭제된 기록");

	private final String description;
}
