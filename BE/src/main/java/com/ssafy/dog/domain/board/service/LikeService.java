package com.ssafy.dog.domain.board.service;

import com.ssafy.dog.common.api.Api;
import com.ssafy.dog.domain.board.dto.LikeReqDto;

public interface LikeService {
	public Api<String> likeChange(LikeReqDto likeReqDto);
}
