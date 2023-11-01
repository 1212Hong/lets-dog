package com.ssafy.dog.domain.board.dto;

import java.util.List;

import com.ssafy.dog.domain.board.enums.Scope;

import lombok.Getter;

@Getter
public class BoardDto {
	private Long userId;
	private String boardTitle;
	private String boardContent;
	private Scope boardScope;
	private int boardLikes;
	private List<String> fileUrlLists;
}
