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
import com.ssafy.dog.domain.board.service.BoardService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class BoardController {
	private final BoardService boardService;

	@PostMapping("/board")
	public Api<String> createBoard(@RequestBody BoardDto boardDto) {
		return boardService.createBoard(boardDto);
	}

	@GetMapping("board")
	public Api<List<BoardDto>> getBoardList(@RequestParam String userLoginId) {
		return boardService.findBoardbyNickname(userLoginId);
	}

	@DeleteMapping("board")
	public Api<String> deleteBoard(@RequestParam Long boardId) {
		return boardService.deleteBoard(boardId);
	}
}
