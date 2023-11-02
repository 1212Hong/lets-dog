package com.ssafy.dog.domain.board.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.dog.common.api.Api;
import com.ssafy.dog.domain.board.dto.BoardDto;
import com.ssafy.dog.domain.board.entity.Board;
import com.ssafy.dog.domain.board.entity.FileUrl;
import com.ssafy.dog.domain.board.repository.BoardRepository;
import com.ssafy.dog.domain.board.repository.FileUrlRepository;
import com.ssafy.dog.domain.user.entity.User;
import com.ssafy.dog.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

	private final UserRepository userRepository;
	private final BoardRepository boardRepository;
	private final FileUrlRepository fileUrlRepository;

	@Transactional
	public Api<String> createBoard(BoardDto boardDto) {

		// user1 -> userId를 통해 user1 객체를 받아온다
		User curUser = userRepository.findByUserId(boardDto.getUserId());

		Board board = Board.builder()
			.user(curUser)
			.boardTitle(boardDto.getBoardTitle())
			.boardContent(boardDto.getBoardContent())
			.boardScope(boardDto.getBoardScope())
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

		return Api.ok(board.getBoardId() + " 번 게시글 등록 성공");
	}

	@Transactional
	public Api<List<BoardDto>> findBoardbyNickname(String userLoginId) {
		List<Board> boardList = boardRepository.findBoardByUser_UserLoginId(userLoginId);
		List<BoardDto> boardDtoList = new ArrayList<>();

		for (Board board : boardList) {
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

}
