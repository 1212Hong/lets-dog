package com.ssafy.dog.domain.user.Model;

import lombok.Getter;

@Getter
public enum UserRole {
	ADMIN("ROLE_ADMIN"),
	USER("ROLE_USER");

	private String value;

	UserRole(String value) {
		this.value = value;
	}
}