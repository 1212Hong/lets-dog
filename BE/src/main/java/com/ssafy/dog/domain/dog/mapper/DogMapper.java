package com.ssafy.dog.domain.dog.mapper;

import org.springframework.stereotype.Component;

import com.ssafy.dog.common.error.UserErrorCode;
import com.ssafy.dog.common.exception.ApiException;
import com.ssafy.dog.domain.dog.dto.request.DogCreateReq;
import com.ssafy.dog.domain.dog.entity.Dog;
import com.ssafy.dog.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DogMapper {
	private final UserRepository userRepository; // 언제 static 이 붙고 언제 final 이 붙어야 하는가

	public Dog toEntity(DogCreateReq dogCreateReq) {
		return Dog.DogBuilder.aDog()
			.withUser(userRepository.findByUserId(dogCreateReq.getUserId())
				.orElseThrow(() -> new ApiException(UserErrorCode.USER_NOT_FOUND)))
			.withDogName(dogCreateReq.getDogName())
			.withDogPicture(dogCreateReq.getDogPicture())
			.withDogBirthdate(dogCreateReq.getDogBirthdate())
			.withDogBreed(dogCreateReq.getDogBreed())
			.withDogDispositionList(dogCreateReq.getDogDispositionList())
			.withDogAboutMe(dogCreateReq.getDogAboutMe())
			.withDogSize(dogCreateReq.getDogSize())
			.build();
	}
}
