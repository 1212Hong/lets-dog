package com.ssafy.dog.domain.board.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.dog.common.api.Api;
import com.ssafy.dog.common.error.BoardErrorCode;
import com.ssafy.dog.common.error.UserErrorCode;
import com.ssafy.dog.common.exception.ApiException;
import com.ssafy.dog.domain.board.dto.LikeReqDto;
import com.ssafy.dog.domain.board.entity.Board;
import com.ssafy.dog.domain.board.entity.LikeEntity;
import com.ssafy.dog.domain.board.enums.FileStatus;
import com.ssafy.dog.domain.board.repository.BoardRepository;
import com.ssafy.dog.domain.board.repository.LikeReposiotry;
import com.ssafy.dog.domain.user.entity.User;
import com.ssafy.dog.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

	private final UserRepository userRepository;
	private final BoardRepository boardRepository;
	private final LikeReposiotry likeReposiotry;

	@Transactional
	public Api<String> likeChange(LikeReqDto likeReqDto) {
		Optional<Board> curBoard = boardRepository.findById(likeReqDto.getBoardId());
		if (curBoard.isEmpty()) {
			throw new ApiException(BoardErrorCode.BOARD_LIST_IS_EMPTY);
		}
		Board board = curBoard.get();

		Optional<User> curUser = userRepository.findByUserNickname(likeReqDto.getUserNickname());
		if (curUser.isEmpty()) {
			throw new ApiException(UserErrorCode.USER_NOT_FOUND);
		}
		User user = curUser.get();

		List<LikeEntity> likeBoardList = likeReposiotry.findByUserUserNickname(likeReqDto.getUserNickname());

		for (LikeEntity likelist : likeBoardList) {
			if (likelist.getLikeStatus() == FileStatus.DELETE) {
				continue;
			}
			if (Objects.equals(likelist.getBoard().getBoardId(), likeReqDto.getBoardId())) {
				likelist.changeLikeStatus();
				likelist.getBoard().decreaseBoardLikes();
				return Api.ok("좋아요 취소 완료");
			}
		}

		LikeEntity likeEntity = LikeEntity.builder()
			.board(board)
			.user(user)
			.build();
		likeReposiotry.save(likeEntity);
		board.increaseBoardLikes();

		return Api.ok("좋아요 적용 완료");
	}
}
