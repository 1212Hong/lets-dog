package com.ssafy.dog.domain.dog.service;

import com.ssafy.dog.common.api.Api;
import com.ssafy.dog.common.error.UserErrorCode;
import com.ssafy.dog.common.exception.ApiException;
import com.ssafy.dog.domain.dog.dto.request.DogCreateReq;
import com.ssafy.dog.domain.dog.dto.response.DogGetRes;
import com.ssafy.dog.domain.dog.entity.Dog;
import com.ssafy.dog.domain.dog.mapper.DogMapper;
import com.ssafy.dog.domain.dog.repository.DogRepository;
import com.ssafy.dog.domain.user.entity.User;
import com.ssafy.dog.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Override
    @Transactional
    public Api<List<DogGetRes>> getDogs(Long userId) {
        Optional<User> user = userRepository.findByUserId(userId);
        if (user.isEmpty()) {
            throw new ApiException(UserErrorCode.USER_NOT_FOUND);
        }

        List<Dog> dogs = dogRepository.findAllByUser(user.get());

        List<DogGetRes> dogGetResList = dogs.stream()
                .map(DogGetRes::toDto)
                .collect(Collectors.toList());

        return Api.ok(dogGetResList);
    }
}
