package com.ssafy.dog.domain.user.dto.request;

import com.ssafy.dog.domain.user.entity.FriendRequest;
import com.ssafy.dog.domain.user.entity.User;
import com.ssafy.dog.domain.user.model.FriendRequestStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class FriendRequestReqDto { // Req 안 받을 때도 쓰는데  이 이름이 맞는가?
    private String senderNickname;
    private String receiverNickname;
    private FriendRequestStatus status;

    @Builder
    public FriendRequestReqDto(String senderNickname, String receiverNickname, FriendRequestStatus status) {
        this.senderNickname = senderNickname;
        this.receiverNickname = receiverNickname;
        this.status = status;
    }

    public static FriendRequest toEntity(User sender, User receiver, FriendRequestStatus status) {
        return FriendRequest.builder()
                .sender(sender)
                .receiver(receiver)
                .status(status)
                .build();
    }
}
