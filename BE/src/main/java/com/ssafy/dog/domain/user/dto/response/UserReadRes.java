package com.ssafy.dog.domain.user.dto.response;

import com.ssafy.dog.domain.user.model.UserGender;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserReadRes {
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
    private Boolean userIsRemoved;

    @Builder
    public UserReadRes(Long userId, String userLoginId, String userNickname, String userPhone, String userPicture,
                       String userAboutMe, UserGender userGender, Double userLatitude, Double userLongitude, String userAddress, Boolean userIsRemoved) {
        this.userId = userId;
        this.userLoginId = userLoginId;
        this.userNickname = userNickname;
        this.userPhone = userPhone;
        this.userPicture = userPicture;
        this.userAboutMe = userAboutMe;
        this.userGender = userGender;
        this.userLatitude = userLatitude;
        this.userLongitude = userLongitude;
        this.userAddress = userAddress;
        this.userIsRemoved = userIsRemoved;
    }
}
