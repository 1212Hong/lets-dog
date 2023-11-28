package com.ssafy.dog.domain.user.dto.request;

import com.ssafy.dog.domain.user.model.UserGender;
import lombok.Getter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import javax.validation.constraints.Size;

@Getter
public class UserUpdateReq {
    private String userNickname;

    @Size(min = 10, max = 11, message = "핸드폰 번호는 10글자 이상 11글자 이하여야 합니다.")
    private String userPhone;

    @Lob
    private String userPicture;

    @Size(max = 200, message = "자기소개는 200글자 이하여야 합니다.")
    private String userAboutMe;

    @Enumerated(EnumType.STRING)
    private UserGender userGender;

    private Double userLatitude;

    private Double userLongitude;

    @Size(max = 255, message = "주소는 255자 이하여야 합니다.")
    private String userAddress;
}
