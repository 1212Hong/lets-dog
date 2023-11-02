package com.ssafy.dog.domain.matching.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.dog.common.error.ErrorCode;
import com.ssafy.dog.common.error.UserErrorCode;
import com.ssafy.dog.common.exception.ApiException;
import com.ssafy.dog.domain.dog.entity.Dog;
import com.ssafy.dog.domain.dog.model.DogDisposition;
import com.ssafy.dog.domain.user.entity.User;
import com.ssafy.dog.domain.user.repository.UserRepository;
import com.ssafy.dog.util.DistanceCalculator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class MatchingServiceImp implements MatchingService {

	private final UserRepository userRepository;
	/*
	모든 유저를 불러와 위도 경도로 거리를 게산하는건 성능적으로 별로
	-> address로 1차 필터링 서울,부산,광주,.. -> 저장된 포맷이 중요
	 */

	@Override
	@Transactional(readOnly = true)
	public List<User> matchingUser(String userLoginId) {
		//TODO 추후 contextHolder에 저장된 userLonginID 사용
		Optional<User> user = userRepository.findByUserLoginId(userLoginId);
		if (user.isEmpty()) {
			throw new ApiException(UserErrorCode.USER_NOT_FOUND, "존재하지 않는 유저입니다.");
		}
		String userAddress = user.get().getUserAddress();
		if (userAddress == null) {
			throw new ApiException(ErrorCode.BAD_REQUEST, "유저 주소 정보가 존재하지 않습니다.");
		}

		String[] address = userAddress.split(" ");
		List<User> matchingCandidates = userRepository.findAllByUserAddressContains(address[0]);
		if (matchingCandidates.isEmpty()) {
			throw new ApiException(ErrorCode.BAD_REQUEST, "같은 지역에 매칭할 수 있는 유저가 없습니다.");
		}
		List<User> filteredByDistance3kmUsers = filteredByDistance3km(user.get(), matchingCandidates);

		return sortByDogDisposition(user.get(), filteredByDistance3kmUsers);
	}

	private List<User> filteredByDistance3km(User user, List<User> candidates) {
		return candidates.stream().filter(it -> {
			double let1 = user.getUserLatitude();
			double lon1 = user.getUserLongitude();
			double let2 = it.getUserLatitude();
			double lon2 = it.getUserLongitude();
			double distance = DistanceCalculator.calculateDistance(let1, lon1, let2, lon2);
			return distance <= 3;
		}).collect(Collectors.toList());
	}

	private List<User> sortByDogDisposition(User user, List<User> candidates) {
		List<Dog> dogs = user.getDogs();
		Set<DogDisposition> userDogDispositionSet = createDogDispositionSet(dogs);
		return candidates.stream()
			.sorted((user1, user2) -> {
				Set<DogDisposition> user1DispositionSet = createDogDispositionSet(user1.getDogs());
				Set<DogDisposition> user2DispositionSet = createDogDispositionSet(user2.getDogs());

				int count1 = countIntersection(new HashSet<>(userDogDispositionSet), user1DispositionSet);
				int count2 = countIntersection(new HashSet<>(userDogDispositionSet), user2DispositionSet);

				return Integer.compare(count2, count1);
			})
			.collect(Collectors.toList());
	}

	private int countIntersection(Set<DogDisposition> dog1, Set<DogDisposition> dog2) {
		dog1.retainAll(dog2);
		return dog1.size();
	}

	private Set<DogDisposition> createDogDispositionSet(List<Dog> dogs) {
		Set<DogDisposition> dogDispositions = new HashSet<>();
		for (Dog dog : dogs) {
			dogDispositions.addAll(dog.getDogDispositionList());
		}
		return dogDispositions;
	}

}
