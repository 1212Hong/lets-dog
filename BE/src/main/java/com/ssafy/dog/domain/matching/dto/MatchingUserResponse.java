package com.ssafy.dog.domain.matching.dto;

import java.util.List;

import com.ssafy.dog.domain.dog.entity.Dog;
import com.ssafy.dog.domain.user.entity.User;
import com.ssafy.dog.domain.user.model.UserGender;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MatchingUserResponse {

	private String userLoginId;

	private String userNickname;

	private String userPicture;

	private String userAboutMe;

	private com.ssafy.dog.domain.user.model.UserGender userGender;

	private Double userLatitude;

	private Double userLongitude;

	private String userAddress;

	private List<Dog> dogs;

	@Builder
	public MatchingUserResponse(String userLoginId, String userNickname, String userPicture, String userAboutMe,
		UserGender userGender, Double userLatitude, Double userLongitude,
		String userAddress, List<Dog> dogs) {
		this.userLoginId = userLoginId;
		this.userNickname = userNickname;
		this.userPicture = userPicture;
		this.userAboutMe = userAboutMe;
		this.userGender = userGender;
		this.userLatitude = userLatitude;
		this.userLongitude = userLongitude;
		this.userAddress = userAddress;
		this.dogs = dogs;
	}

	public static MatchingUserResponse toResponse(User user) {
		return MatchingUserResponse.builder()
			.userLoginId(user.getUserLoginId())
			.userNickname(user.getUserNickname())
			.userPicture(user.getUserPicture())
			.userAboutMe(user.getUserAboutMe())
			.userGender(user.getUserGender())
			.userLatitude(user.getUserLatitude())
			.userLongitude(user.getUserLongitude())
			.dogs(user.getDogs())
			.build();
	}
}
