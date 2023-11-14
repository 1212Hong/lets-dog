package com.ssafy.dog.domain.board.service;

import java.util.List;

import com.ssafy.dog.common.api.Api;
import com.ssafy.dog.domain.board.dto.CommentDto;
import com.ssafy.dog.domain.board.dto.CommentResDto;

public interface CommentService {
	public Api<String> createComment(CommentDto commentDto);

	public Api<List<CommentResDto>> findCommentsbyBoardId(Long boardId);

	public Api<String> deleteComment(Long commentId);
}
