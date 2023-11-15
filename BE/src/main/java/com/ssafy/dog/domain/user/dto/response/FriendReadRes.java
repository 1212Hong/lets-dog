package com.ssafy.dog.domain.user.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class FriendReadRes {
    private String userNickname;
    private String userPicture;

    @Builder
    public FriendReadRes(String userNickname, String userPicture) {
        this.userNickname = userNickname;
        this.userPicture = userPicture;
    }
}
