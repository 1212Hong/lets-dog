package com.ssafy.dog.domain.board.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentResDto {
	private Long boardId;
	private Long commentId;
	@NotBlank
	@Max(value = 100)
	private String commentContent;
	private String userNickname;
	private int commentLikes;
	private String userProfileUrl;

	@Builder
	public CommentResDto(Long boardId, Long commentId, String commentContent, String userNickname, int commentLikes,
		String userProfileUrl) {
		this.boardId = boardId;
		this.commentContent = commentContent;
		this.userNickname = userNickname;
		this.commentLikes = commentLikes;
		this.commentId = commentId;
		this.userProfileUrl = userProfileUrl;
	}

}
