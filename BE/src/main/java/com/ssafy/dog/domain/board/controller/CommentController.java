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
import com.ssafy.dog.domain.board.dto.CommentDto;
import com.ssafy.dog.domain.board.dto.CommentIdDto;
import com.ssafy.dog.domain.board.dto.CommentResDto;
import com.ssafy.dog.domain.board.service.CommentService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {
	private final CommentService commentService;

	@PostMapping("/comment")
	@Operation(summary = "댓글 작성")
	public Api<String> createComment(@RequestBody CommentDto commentDto) {
		return commentService.createComment(commentDto);
	}

	@GetMapping("/comment")
	@Operation(summary = "BoardId 로 댓글 목록 불러오기")
	public Api<List<CommentResDto>> getComments(@RequestParam Long boardId) {
		return commentService.findCommentsbyBoardId(boardId);
	}

	@DeleteMapping("/comment")
	@Operation(summary = "댓글 id로 댓글 삭제")
	public Api<String> deleteComment(@RequestBody CommentIdDto commentIdDto) {
		return commentService.deleteComment(commentIdDto);
	}
}
