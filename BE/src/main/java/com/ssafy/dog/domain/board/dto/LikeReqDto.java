package com.ssafy.dog.domain.board.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class LikeReqDto {
	private Long boardId;
	private String userNickname;

	@Builder
	public LikeReqDto(Long boardId, String userNickname) {
		this.boardId = boardId;
		this.userNickname = userNickname;
	}

	public LikeReqDto() {

	}
}
