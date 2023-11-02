package com.ssafy.dog.domain.board.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.dog.common.api.Api;
import com.ssafy.dog.common.error.BoardErrorCode;
import com.ssafy.dog.common.error.UserErrorCode;
import com.ssafy.dog.common.exception.ApiException;
import com.ssafy.dog.domain.board.dto.BoardDto;
import com.ssafy.dog.domain.board.entity.Board;
import com.ssafy.dog.domain.board.entity.FileUrl;
import com.ssafy.dog.domain.board.enums.FileStatus;
import com.ssafy.dog.domain.board.repository.BoardRepository;
import com.ssafy.dog.domain.board.repository.FileUrlRepository;
import com.ssafy.dog.domain.user.entity.User;
import com.ssafy.dog.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

	private final UserRepository userRepository;
	private final BoardRepository boardRepository;
	private final FileUrlRepository fileUrlRepository;

	@Transactional
	public Api<String> createBoard(BoardDto boardDto) {

		// user1 -> userId를 통해 user1 객체를 받아온다
		Optional<User> curUser = userRepository.findByUserId(boardDto.getUserId());
		User currentUser = curUser.orElseThrow(() -> new ApiException(UserErrorCode.USER_NOT_FOUND));

		User user = curUser.get();
		if (boardDto.getBoardTitle().length() > 50) {
			throw new ApiException(BoardErrorCode.TITLE_TOO_LONG);
		}
		if (boardDto.getBoardTitle().isEmpty()) {
			throw new ApiException(BoardErrorCode.TITLE_NOT_FOUND);
		}
		if (boardDto.getBoardContent().length() > 200) {
			throw new ApiException(BoardErrorCode.CONTENT_TOO_LONG);
		}
		if (boardDto.getBoardContent().isEmpty()) {
			throw new ApiException(BoardErrorCode.CONTENT_NOT_FOUND);
		}

		Board board = Board.builder()
			.user(user)
			.boardTitle(boardDto.getBoardTitle())
			.boardContent(boardDto.getBoardContent())
			.boardScope(boardDto.getBoardScope())
			.boardStatus(FileStatus.USE)
			.build();
		boardRepository.save(board);

		// 받아온 file List를 db에 등록한다
		for (String urlList : boardDto.getFileUrlLists()) {
			FileUrl fileUrl = FileUrl.builder()
				.boardId(board)
				.fileUrl(urlList)
				.build();
			fileUrlRepository.save(fileUrl);
		}
		return Api.ok(board.getBoardId() + " 번째 게시글 등록 완료");
	}

	@Transactional
	public Api<List<BoardDto>> findBoardbyNickname(String userLoginId) {
		Optional<User> curUser = userRepository.findUserByUserLoginId(userLoginId);
		User currentUser = curUser.orElseThrow(() -> new ApiException(UserErrorCode.USER_NOT_FOUND));

		List<Board> boardList = boardRepository.findBoardByUser_UserLoginId(userLoginId);
		List<BoardDto> boardDtoList = new ArrayList<>();

		if (boardList.isEmpty()) {
			throw new ApiException(BoardErrorCode.BOARD_LIST_IS_EMPTY);
		}

		for (Board board : boardList) {
			if (board.getBoardStatus() == FileStatus.DELETE) {
				continue;
			}
			BoardDto boardDto = BoardDto.builder()
				.userId(board.getUser().getUserId())
				.boardTitle(board.getBoardTitle())
				.boardContent(board.getBoardContent())
				.boardScope(board.getBoardScope())
				.boardLikes(board.getBoardLikes())
				.fileUrlLists(board.getFileUrlLists().stream().map(FileUrl::getFileUrl).collect(Collectors.toList()))
				.build();
			boardDtoList.add(boardDto);
		}

		return Api.ok(boardDtoList);
	}

	@Transactional
	public Api<String> deleteBoard(Long boardId) {
		Optional<Board> boardOptional = boardRepository.findById(boardId);
		Board board = boardOptional.orElseThrow(() -> new ApiException(BoardErrorCode.BOARD_LIST_IS_EMPTY));
		if (board.getBoardStatus() == FileStatus.DELETE) {
			throw new ApiException(BoardErrorCode.BOARD_LIST_IS_EMPTY);
		}
		List<FileUrl> fileUrls = board.getFileUrlLists();
		for (FileUrl fileUrl : fileUrls) {
			fileUrl.removeBoard();
		}

		// 게시글을 삭제한다.
		board.removeBoard();
		return Api.ok(boardId + " 번 게시글 삭제 완료");
	}
}
