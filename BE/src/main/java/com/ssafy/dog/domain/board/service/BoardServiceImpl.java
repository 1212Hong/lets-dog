package com.ssafy.dog.domain.board.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.dog.common.api.Api;
import com.ssafy.dog.domain.board.dto.BoardDto;
import com.ssafy.dog.domain.board.entity.Board;
import com.ssafy.dog.domain.board.repository.BoardRepository;
import com.ssafy.dog.domain.user.entity.User;
import com.ssafy.dog.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

	private final UserRepository userRepository;
	private final BoardRepository boardRepository;

	@Transactional
	public Api<?> createBoard(BoardDto boardDto) {

		// user1 -> userId를 통해 user1 객체를 받아온다
		User curUser = userRepository.findByUserId(boardDto.getUserId());

		Board board = Board.builder()
			.user(curUser)
			.boardTitle(boardDto.getBoardTitle())
			.boardContent(boardDto.getBoardContent())
			.boardScope(boardDto.getBoardScope())
			.build();
		boardRepository.save(board);

		return Api.ok(board.getBoardId() + "게시글 등록 성공");
	}

}
