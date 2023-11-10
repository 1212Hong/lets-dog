package com.ssafy.dog.domain.dog.service;

import com.ssafy.dog.common.api.Api;
import com.ssafy.dog.domain.dog.dto.request.DogCreateReq;
import com.ssafy.dog.domain.dog.dto.response.DogGetRes;
import com.ssafy.dog.domain.dog.entity.Dog;

import java.util.List;

public interface DogService {

    Dog create(DogCreateReq dogCreateReq);

    Api<List<DogGetRes>> getDogs(Long userId);
}
