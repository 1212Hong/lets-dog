package com.ssafy.dog.domain.board.service;

import com.ssafy.dog.common.api.Api;
import com.ssafy.dog.domain.board.dto.BoardIdReqDto;

public interface LikeService {
	public Api<String> likeChange(BoardIdReqDto boardIdReqDto);

	public Api<String> deleteLike(BoardIdReqDto boardIdReqDto);
}
