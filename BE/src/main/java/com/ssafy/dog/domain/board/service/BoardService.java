package com.ssafy.dog.domain.board.service;

import java.util.List;

import com.ssafy.dog.common.api.Api;
import com.ssafy.dog.domain.board.dto.BoardDto;
import com.ssafy.dog.domain.board.dto.BoardIdReqDto;
import com.ssafy.dog.domain.board.dto.BoardReqDto;

public interface BoardService {
	public Api<String> createBoard(BoardReqDto boardDto);

	public Api<List<BoardDto>> findBoardbyNickname(String userLoginId);

	public Api<List<BoardDto>> findBoardNeararea(double userLatitude, double userLongitude);

	public Api<String> deleteBoard(BoardIdReqDto boardIdReqDto);

}
