package com.ssafy.dog.domain.user.dto.request;

import com.ssafy.dog.domain.user.model.UserGender;

import lombok.Getter;

@Getter
public class UserUpdateReq {
	private String userNickname;
	private String userPhone;
	private String userPicture;
	private String userAboutMe;
	private UserGender userGender;
	private Double userLatitude;
	private Double userLongitude;
	private String userAddress;
}
