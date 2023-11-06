package com.ssafy.dog.domain.board.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CommentDto {
	private Long boardId;
	@NotBlank
	@Max(value = 50)
	private String commentContent;
	private String userNickname;

	@Builder
	public CommentDto(Long boardId, String commentContent, String userNickname) {
		this.boardId = boardId;
		this.commentContent = commentContent;
		this.userNickname = userNickname;
	}

	public CommentDto() {

	}
}
