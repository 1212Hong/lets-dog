package com.ssafy.dog.domain.board.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.dog.common.api.Api;
import com.ssafy.dog.domain.board.dto.BoardDto;
import com.ssafy.dog.domain.board.dto.BoardReqDto;
import com.ssafy.dog.domain.board.service.BoardService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class BoardController {
	private final BoardService boardService;

	@PostMapping("/board")
	@Operation(summary = "게시글 작성")
	public Api<String> createBoard(@RequestBody BoardReqDto boardDto) {
		return boardService.createBoard(boardDto);
	}

	@GetMapping("/board")
	@Operation(summary = "UserNickname이 작성한 게시글 목록 불러오기")
	public Api<List<BoardDto>> getBoardList(@RequestParam String userNickname) {
		return boardService.findBoardbyNickname(userNickname);
	}

	@DeleteMapping("/board")
	@Operation(summary = "BoardId 로 게시글 삭제 하기")
	public Api<String> deleteBoard(@RequestParam Long boardId) {
		return boardService.deleteBoard(boardId);
	}

	@GetMapping("/boardnear")
	@Operation(summary = "내 주변 사용자들 게시글 목록 가져오기")
	public Api<List<BoardDto>> getBoardListNearby(@RequestParam double userLatitude,
		@RequestParam double userLongitude) {
		return boardService.findBoardNeararea(userLatitude, userLongitude);
	}
}
