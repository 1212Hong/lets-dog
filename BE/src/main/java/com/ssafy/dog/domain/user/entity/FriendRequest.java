package com.ssafy.dog.domain.user.entity;

import com.ssafy.dog.common.auditing.BaseTimeEntity;
import com.ssafy.dog.domain.user.model.FriendRequestStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "friend_request")
public class FriendRequest extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long friendRequestId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", referencedColumnName = "user_id")
    private User sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", referencedColumnName = "user_id")
    private User receiver;

    @Enumerated(EnumType.STRING)
    private FriendRequestStatus status; // PENDING, ACCEPTED, DECLINED

    @Builder
    public FriendRequest(User sender, User receiver, FriendRequestStatus status) {
        this.sender = sender;
        this.receiver = receiver;
        this.status = status;
    }
}
