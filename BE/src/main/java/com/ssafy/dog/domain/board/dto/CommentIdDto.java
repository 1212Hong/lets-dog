package com.ssafy.dog.domain.board.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentIdDto {
	private Long commentId;

	@Builder
	public CommentIdDto(Long commentId) {
		this.commentId = commentId;
	}

}
