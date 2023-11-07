package com.ssafy.dog.domain.user.dto.response;

import com.ssafy.dog.domain.user.model.FriendRequestStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class FriendRequestResDto { // 리팩토링 시 시간도 전달할 것
    private String senderNickname;
    private String receiverNickname;
    private FriendRequestStatus status;

    @Builder
    public FriendRequestResDto(String senderNickname, String receiverNickname, FriendRequestStatus status) {
        this.senderNickname = senderNickname;
        this.receiverNickname = receiverNickname;
        this.status = status;
    }
}
