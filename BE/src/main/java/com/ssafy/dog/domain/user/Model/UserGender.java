package com.ssafy.dog.domain.user.model;

import lombok.Getter;

@Getter
public enum UserGender {
	MALE("M"),
	FEMALE("F");

	private String value;

	UserGender(String value) {
		this.value = value;
	}
}
