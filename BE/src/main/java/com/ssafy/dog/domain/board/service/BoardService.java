package com.ssafy.dog.domain.board.service;

import com.ssafy.dog.common.api.Api;
import com.ssafy.dog.domain.board.dto.BoardDto;

public interface BoardService {
	public Api<?> createBoard(BoardDto boardDto);
}
