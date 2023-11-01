package com.ssafy.dog.domain.board.dto;

import java.util.List;

import com.ssafy.dog.domain.board.enums.Scope;

import lombok.Builder;
import lombok.Getter;

@Getter
public class BoardDto {
	private Long userId;
	private String boardTitle;
	private String boardContent;
	private Scope boardScope;
	private int boardLikes;
	private List<String> fileUrlLists;

	@Builder
	public BoardDto(Long userId, String boardTitle, String boardContent, Scope boardScope, int boardLikes,
		List<String> fileUrlLists) {
		this.userId = userId;
		this.boardTitle = boardTitle;
		this.boardContent = boardContent;
		this.boardScope = boardScope;
		this.boardLikes = boardLikes;
		this.fileUrlLists = fileUrlLists;
	}

	public BoardDto() {
		// 기본 생성자 구현 (빈 생성자)
	}
}
