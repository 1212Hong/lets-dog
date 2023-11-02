package com.ssafy.dog.domain.board.dto;

import java.util.List;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;

import com.ssafy.dog.domain.board.enums.FileStatus;
import com.ssafy.dog.domain.board.enums.Scope;

import lombok.Builder;
import lombok.Getter;

@Getter
public class BoardDto {
	private Long userId;
	@NotBlank(message = "제목을 입력해주세요")
	@Max(value = 50, message = "제목은 50자 이내여야 합니다")
	private String boardTitle;
	@NotBlank(message = "내용을 입력해주세요")
	@Max(value = 200, message = "게시글은 200자 이내여야 합니다")
	private String boardContent;
	private Scope boardScope;
	private FileStatus boardStatus;
	private int boardLikes;
	private List<String> fileUrlLists;

	@Builder
	public BoardDto(Long userId, String boardTitle, String boardContent, Scope boardScope, int boardLikes,
		FileStatus boardStatus, List<String> fileUrlLists) {
		this.userId = userId;
		this.boardTitle = boardTitle;
		this.boardContent = boardContent;
		this.boardScope = boardScope;
		this.boardLikes = boardLikes;
		this.boardStatus = boardStatus;
		this.fileUrlLists = fileUrlLists;
	}

	public BoardDto() {
		// 기본 생성자 구현 (빈 생성자)
	}
}
