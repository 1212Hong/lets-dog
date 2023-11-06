package com.ssafy.dog.domain.board.dto;

import java.util.List;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;

import com.ssafy.dog.domain.board.enums.Scope;

import lombok.Builder;
import lombok.Getter;

@Getter
public class BoardDto {
	private String userNickname;
	private Long boardId;
	@NotBlank(message = "내용을 입력해주세요")
	@Max(value = 200, message = "게시글은 200자 이내여야 합니다")
	private String boardContent;
	private Scope boardScope;
	private int boardLikes;
	private List<String> fileUrlLists;
	private int boardComments;

	@Builder
	public BoardDto(String userNickname, String boardContent, Scope boardScope, int boardLikes,
		List<String> fileUrlLists, int boardComments, Long boardId) {
		this.userNickname = userNickname;
		this.boardContent = boardContent;
		this.boardScope = boardScope;
		this.boardLikes = boardLikes;
		this.fileUrlLists = fileUrlLists;
		this.boardComments = boardComments;
		this.boardId = boardId;
	}

	public BoardDto() {
		// 기본 생성자 구현 (빈 생성자)
	}
}
