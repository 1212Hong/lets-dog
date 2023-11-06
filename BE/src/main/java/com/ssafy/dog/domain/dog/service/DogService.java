package com.ssafy.dog.domain.dog.service;

import com.ssafy.dog.domain.dog.dto.request.DogCreateReq;
import com.ssafy.dog.domain.dog.entity.Dog;

public interface DogService {

	Dog create(DogCreateReq dogCreateReq);
}
