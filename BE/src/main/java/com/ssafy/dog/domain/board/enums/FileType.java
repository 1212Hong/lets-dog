package com.ssafy.dog.domain.board.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum FileType {
	IMAGE("사진"),
	VIDEO("영상");

	private final String description;
}
