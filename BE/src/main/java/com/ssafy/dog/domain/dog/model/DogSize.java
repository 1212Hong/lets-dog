package com.ssafy.dog.domain.dog.model;

import lombok.Getter;

@Getter
public enum DogSize {
	SMALL("small"),
	MEDIUM("medium"),
	LARGE("large");

	private String value;

	DogSize(String value) {
		this.value = value;
	}
}
