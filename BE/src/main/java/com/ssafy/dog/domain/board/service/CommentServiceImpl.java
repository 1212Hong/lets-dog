package com.ssafy.dog.domain.board.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.dog.common.api.Api;
import com.ssafy.dog.common.error.BoardErrorCode;
import com.ssafy.dog.common.error.CommentErrorCode;
import com.ssafy.dog.common.error.UserErrorCode;
import com.ssafy.dog.common.exception.ApiException;
import com.ssafy.dog.domain.board.dto.CommentDto;
import com.ssafy.dog.domain.board.dto.CommentResDto;
import com.ssafy.dog.domain.board.entity.Board;
import com.ssafy.dog.domain.board.entity.Comment;
import com.ssafy.dog.domain.board.enums.FileStatus;
import com.ssafy.dog.domain.board.repository.BoardRepository;
import com.ssafy.dog.domain.board.repository.CommentRepository;
import com.ssafy.dog.domain.user.entity.User;
import com.ssafy.dog.domain.user.repository.UserRepository;
import com.ssafy.dog.util.SecurityUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

	private final BoardRepository boardRepository;
	private final UserRepository userRepository;
	private final CommentRepository commentRepository;

	@Transactional
	public Api<String> createComment(CommentDto commentDto) {
		String userNickname = SecurityUtils.getUser().getUserNickname();
		Optional<Board> board = boardRepository.findById(commentDto.getBoardId());
		if (board.isEmpty()) {
			throw new ApiException(BoardErrorCode.BOARD_LIST_IS_EMPTY);
		}
		Board curBoard = board.get();
		if (curBoard.getBoardStatus() == FileStatus.DELETE) {
			throw new ApiException(BoardErrorCode.BOARD_LIST_IS_EMPTY);
		}

		Optional<User> user = userRepository.findByUserNickname(userNickname);
		if (user.isEmpty()) {
			throw new ApiException(UserErrorCode.USER_NOT_FOUND);
		}
		User curUser = user.get();

		if (commentDto.getCommentContent().length() > 100) {
			throw new ApiException(CommentErrorCode.COMMENT_TOO_LONG);
		}
		if (commentDto.getCommentContent().isEmpty()) {
			throw new ApiException(CommentErrorCode.COMMENT_NOT_FOUND);
		}

		Comment comment = Comment.builder()
			.user(curUser)
			.board(curBoard)
			.commentContent(commentDto.getCommentContent())
			.commentStatus(FileStatus.USE)
			.build();
		commentRepository.save(comment);
		curBoard.increaseBoardComment();

		return Api.ok(curBoard.getBoardId() + " 번 게시물에 " + curUser.getUserNickname() + " 의 댓글 등록 성공");
	}

	@Transactional(readOnly = true)
	public Api<List<CommentResDto>> findCommentsbyBoardId(Long boardId) {
		Optional<Board> board = boardRepository.findById(boardId);
		if (board.isEmpty()) {
			throw new ApiException(BoardErrorCode.BOARD_LIST_IS_EMPTY);
		}
		Board curBoard = board.get();
		if (curBoard.getBoardStatus() == FileStatus.DELETE) {
			throw new ApiException(BoardErrorCode.BOARD_LIST_IS_EMPTY);
		}

		List<CommentResDto> commentList = new ArrayList<>();
		for (Comment comment : curBoard.getCommentList()) {
			if (comment.getCommentStatus() == FileStatus.USE) {
				CommentResDto comment1 = CommentResDto.builder()
					.boardId(curBoard.getBoardId())
					.userNickname(comment.getUser().getUserNickname())
					.commentContent(comment.getCommentContent())
					.commentLikes(comment.getCommentLikes())
					.commentId(comment.getCommentId())
					.userProfileUrl(comment.getUser().getUserPicture())
					.build();
				commentList.add(comment1);
			}
		}
		return Api.ok(commentList);
	}

	@Transactional
	public Api<String> deleteComment(Long commentId) {
		Long userId = SecurityUtils.getUserId();
		Optional<Comment> comment = commentRepository.findById(commentId);
		if (comment.isEmpty()) {
			throw new ApiException(BoardErrorCode.COMMENT_NOT_FOUND);
		}
		Comment curcomment = comment.get();
		if (curcomment.getCommentStatus() == FileStatus.DELETE) {
			throw new ApiException(BoardErrorCode.COMMENT_NOT_FOUND);
		}
		if (!Objects.equals(curcomment.getUser().getUserId(), userId)) {
			throw new ApiException(BoardErrorCode.USER_NOT_MATCH);
		}
		curcomment.removeComment();
		curcomment.getBoard().decreaseBoardComment();
		return Api.ok("댓글 삭제 완료");
	}
}
