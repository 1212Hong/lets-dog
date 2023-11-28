package com.ssafy.dog.domain.board.service;

import static com.ssafy.dog.util.DistanceCalculator.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.dog.common.api.Api;
import com.ssafy.dog.common.error.BoardErrorCode;
import com.ssafy.dog.common.error.GpsErrorCode;
import com.ssafy.dog.common.error.UserErrorCode;
import com.ssafy.dog.common.exception.ApiException;
import com.ssafy.dog.domain.board.dto.BoardDto;
import com.ssafy.dog.domain.board.dto.BoardReqDto;
import com.ssafy.dog.domain.board.entity.Board;
import com.ssafy.dog.domain.board.entity.Comment;
import com.ssafy.dog.domain.board.entity.FileUrl;
import com.ssafy.dog.domain.board.entity.LikeEntity;
import com.ssafy.dog.domain.board.enums.FileStatus;
import com.ssafy.dog.domain.board.enums.Scope;
import com.ssafy.dog.domain.board.repository.BoardRepository;
import com.ssafy.dog.domain.board.repository.FileUrlRepository;
import com.ssafy.dog.domain.board.repository.LikeReposiotry;
import com.ssafy.dog.domain.user.entity.User;
import com.ssafy.dog.domain.user.repository.FriendshipRepository;
import com.ssafy.dog.domain.user.repository.UserRepository;
import com.ssafy.dog.util.SecurityUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

	private final UserRepository userRepository;
	private final BoardRepository boardRepository;
	private final FileUrlRepository fileUrlRepository;
	private final LikeReposiotry likeReposiotry;
	private final FriendshipRepository friendshipRepository;

	Sort sort = Sort.by(Sort.Direction.DESC, "createdDate");

	boolean checkLikeBoard(Long boardId, String userNickname) {
		List<LikeEntity> likeList = likeReposiotry.findByUserUserNickname(userNickname);
		for (LikeEntity likelist : likeList) {
			if (likelist.getLikeStatus() == FileStatus.DELETE) {
				continue;
			}
			if (Objects.equals(likelist.getBoard().getBoardId(), boardId)) {
				return true;
			}
		}
		return false;
	}

	private boolean checkFriend(User user, User friend) {
		return friendshipRepository.findByUserAndFriend(user, friend).isPresent();
	}

	@Transactional
	public Api<String> createBoard(BoardReqDto boardDto) {
		String userNickname = SecurityUtils.getUser().getUserNickname();
		Optional<User> curUser = userRepository.findByUserNickname(userNickname);
		if (curUser.isEmpty()) {
			throw new ApiException(UserErrorCode.USER_NOT_FOUND);
		}
		if (boardDto.getBoardContent().length() > 200) {
			throw new ApiException(BoardErrorCode.CONTENT_TOO_LONG);
		}
		if (boardDto.getBoardContent().isEmpty()) {
			throw new ApiException(BoardErrorCode.CONTENT_NOT_FOUND);
		}

		User user = curUser.get();
		Board board = Board.builder()
			.user(user)
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
	public Api<List<BoardDto>> findBoardbyNickname(String userNickname) {
		String viewUserNickname = SecurityUtils.getUser().getUserNickname();

		Optional<User> curUser = userRepository.findUserByUserNickname(userNickname);
		User currentUser = curUser.orElseThrow(() -> new ApiException(UserErrorCode.USER_NOT_FOUND));
		Optional<User> curUser1 = userRepository.findUserByUserNickname(viewUserNickname);
		User viewUser = curUser1.orElseThrow(() -> new ApiException(UserErrorCode.USER_NOT_FOUND));

		List<Board> boardList = boardRepository.findBoardByUserUserNickname(userNickname, sort);

		List<BoardDto> boardDtoList = new ArrayList<>();

		// 게시물 없을시 빈배열 보내기로 합의함. 11.06 09:25
		// if (boardList.isEmpty()) {
		// 	throw new ApiException(BoardErrorCode.BOARD_LIST_IS_EMPTY);
		// }

		for (Board board : boardList) {
			if (board.getBoardStatus() == FileStatus.DELETE) {
				continue;
			}
			Scope scope = board.getBoardScope();
			// 게시물 공개범위 추후 적용
//			if (scope == Scope.MeOnly) {
//				if (!(board.getUser() == viewUser)) {
//					continue;
//				}
//			}
//			if (scope == Scope.Friends) {
//				if (!checkFriend(board.getUser(), viewUser) && !(board.getUser() == viewUser)) {
//					continue;
//				}
//			}
			BoardDto boardDto = BoardDto.builder()
				.userNickname(board.getUser().getUserNickname())
				.boardId(board.getBoardId())
				.boardContent(board.getBoardContent())
				.boardScope(board.getBoardScope())
				.boardLikes(board.getBoardLikes())
				.boardComments(board.getBoardComments())
				.likecheck(checkLikeBoard(board.getBoardId(), viewUserNickname))
				.profileUrl(board.getUser().getUserPicture())
				.fileUrlLists(board.getFileUrlLists().stream().map(FileUrl::getFileUrl).collect(Collectors.toList()))
				.build();
			boardDtoList.add(boardDto);
		}

		return Api.ok(boardDtoList);
	}

	@Transactional
	public Api<String> deleteBoard(Long boardId) {
		Long userId = SecurityUtils.getUserId();
		Optional<Board> boardOptional = boardRepository.findById(boardId);
		Board board = boardOptional.orElseThrow(() -> new ApiException(BoardErrorCode.BOARD_LIST_IS_EMPTY));
		if (board.getBoardStatus() == FileStatus.DELETE) {
			throw new ApiException(BoardErrorCode.BOARD_LIST_IS_EMPTY);
		}
		if (!Objects.equals(board.getUser().getUserId(), userId)) {
			throw new ApiException(BoardErrorCode.USER_NOT_MATCH);
		}

		List<FileUrl> fileUrls = board.getFileUrlLists();
		for (FileUrl fileUrl : fileUrls) {
			fileUrl.removeFile();
		}
		List<Comment> commentLists = board.getCommentList();
		for (Comment comment : commentLists) {
			comment.removeComment();
		}

		// 게시글을 삭제한다.
		board.removeBoard();
		return Api.ok(boardId + " 번 게시글 삭제 완료");
	}

	@Transactional
	public Api<List<BoardDto>> findBoardNeararea(double userLatitude, double userLongitude) {
		String viewUserNickname = SecurityUtils.getUser().getUserNickname();
		List<Board> boardList = boardRepository.findAll(sort);
		Optional<User> curUser1 = userRepository.findUserByUserNickname(viewUserNickname);
		User viewUser = curUser1.orElseThrow(() -> new ApiException(UserErrorCode.USER_NOT_FOUND));

		// 대한민국 위경도 기준
		// 북위 : 33.10000000 ~ 38.45000000
		// 동경 : 125.06666667 ~ 131.87222222

		List<BoardDto> boardDtoList = new ArrayList<>();
		if (!(33.10000000 < userLatitude && userLatitude < 38.45000000)) {
			throw new ApiException(GpsErrorCode.GPS_POINT_NOT_IN_KOREA);
		}
		if (!(125.06666667 < userLongitude && userLongitude < 131.87222222)) {
			throw new ApiException(GpsErrorCode.GPS_POINT_NOT_IN_KOREA);
		}

		for (Board board : boardList) {
			if (board.getBoardStatus() == FileStatus.DELETE) {
				continue;
			}
			if (board.getUser().getUserLatitude() == null || board.getUser().getUserLongitude() == null) {
				continue;
			}
			if (calculateDistance(userLatitude, userLongitude,
				board.getUser().getUserLatitude(), board.getUser().getUserLongitude()) > 1.5) {
				continue;
			}
			// 게시물 공개 범위 나중에 다시 협의
//			if (board.getUser() == viewUser) {
//				continue;
//			}
//			if (board.getBoardScope() == Scope.MeOnly) {
//				continue;
//			}
//			if (board.getBoardScope() == Scope.Friends) {
//				if (!checkFriend(board.getUser(), viewUser)) {
//					continue;
//				}
//			}
			BoardDto boardDto = BoardDto.builder()
				.userNickname(board.getUser().getUserNickname())
				.boardId(board.getBoardId())
				.boardContent(board.getBoardContent())
				.boardScope(board.getBoardScope())
				.boardLikes(board.getBoardLikes())
				.boardComments(board.getBoardComments())
				.likecheck(checkLikeBoard(board.getBoardId(), viewUserNickname))
				.profileUrl(board.getUser().getUserPicture())
				.fileUrlLists(
					board.getFileUrlLists().stream().map(FileUrl::getFileUrl).collect(Collectors.toList()))
				.build();
			boardDtoList.add(boardDto);
		}

		return Api.ok(boardDtoList);
	}

}
