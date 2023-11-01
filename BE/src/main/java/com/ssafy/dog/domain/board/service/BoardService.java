package com.ssafy.dog.domain.board.service;

import java.util.List;

import com.ssafy.dog.common.api.Api;
import com.ssafy.dog.domain.board.dto.BoardDto;

public interface BoardService {
	public Api<String> createBoard(BoardDto boardDto);

	public Api<List<BoardDto>> findBoardbyNickname(String userLoginId);
}
