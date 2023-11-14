package com.ssafy.dog.domain.user.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class FriendUnfriendRes {
    private String senderNickname;
    private String receiverNickname;

    @Builder
    public FriendUnfriendRes(String senderNickname, String receiverNickname) {
        this.senderNickname = senderNickname;
        this.receiverNickname = receiverNickname;
    }
}
