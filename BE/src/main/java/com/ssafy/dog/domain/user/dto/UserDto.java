package com.ssafy.dog.domain.user.dto;

import com.ssafy.dog.domain.user.model.UserGender;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserDto {
	private Long userId;
	private String userLoginId;
	private String userNickname;
	private String userPhone;
	private String userPicture;
	private String userAboutMe;
	private UserGender userGender;
	private Double userLatitude;
	private Double userLongitude;
	private String userAddress;
}
