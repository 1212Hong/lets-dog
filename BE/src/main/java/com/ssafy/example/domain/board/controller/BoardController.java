package com.ssafy.example.domain.board.controller;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.example.common.api.Api;
import com.ssafy.example.domain.board.dto.request.BoardCreateRequest;
import com.ssafy.example.domain.board.dto.response.BoardCreateResponse;
import com.ssafy.example.domain.board.service.BoardService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/board")
public class BoardController {
	private final BoardService boardService;

	@PostMapping
	@ExceptionHandler(Exception.class)
	public Api<BoardCreateResponse> boardCreate(@RequestBody BoardCreateRequest boardCreateRequest) {
		return Api.ok(boardService.boardCreate(boardCreateRequest));
	}
}
