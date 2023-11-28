package com.ssafy.dog.domain.board.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class BoardIdReqDto {
	private Long boardId;

	@Builder
	public BoardIdReqDto(Long boardId, String userNickname) {
		this.boardId = boardId;
	}

	public BoardIdReqDto() {

	}
}
