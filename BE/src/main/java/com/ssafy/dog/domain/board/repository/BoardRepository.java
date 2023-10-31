package com.ssafy.dog.domain.board.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.dog.domain.board.entity.Board;

public interface BoardRepository extends JpaRepository<Board, Long> {
	Optional<Board> findBoardByUser_UserNickname(String nickName);
}
