package com.ssafy.dog.domain.board.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.dog.domain.board.entity.Board;

public interface BoardRepository extends JpaRepository<Board, Long> {
	List<Board> findBoardByUser_UserLoginId(String userLoginId);

	Optional<Board> findBoardByBoardId(Long boardId);

	Board deleteBoardByBoardId(Long boardId);
}
