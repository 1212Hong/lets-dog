package com.ssafy.dog.domain.board.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CommentResDto {
	private Long boardId;
	private Long commentId;
	@NotBlank
	@Max(value = 100)
	private String commentContent;
	private String userNickname;
	private int commentLikes;

	@Builder
	public CommentResDto(Long boardId, Long commentId, String commentContent, String userNickname, int commentLikes) {
		this.boardId = boardId;
		this.commentContent = commentContent;
		this.userNickname = userNickname;
		this.commentLikes = commentLikes;
		this.commentId = commentId;
	}

	public CommentResDto() {

	}
}
