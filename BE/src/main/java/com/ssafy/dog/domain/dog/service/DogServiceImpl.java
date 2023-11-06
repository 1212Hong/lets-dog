package com.ssafy.dog.domain.dog.service;

import org.springframework.stereotype.Service;

import com.ssafy.dog.domain.dog.dto.request.DogCreateReq;
import com.ssafy.dog.domain.dog.entity.Dog;
import com.ssafy.dog.domain.dog.mapper.DogMapper;
import com.ssafy.dog.domain.dog.repository.DogRepository;
import com.ssafy.dog.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DogServiceImpl implements DogService {
	private final DogRepository dogRepository;
	private final UserRepository userRepository;
	private final DogMapper dogMapper;

	@Override
	public Dog create(DogCreateReq dogCreateReq) {
		Dog dog = dogMapper.toEntity(dogCreateReq);
		return dogRepository.save(dog);
	}
}
