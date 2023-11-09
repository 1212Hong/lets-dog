package com.ssafy.dog.domain.user.entity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ssafy.dog.common.auditing.BaseTimeEntity;
import com.ssafy.dog.domain.user.model.FriendRequestStatus;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

	public void changeStatus(FriendRequestStatus status) {
		this.status = status;
	}

	@Builder
	public FriendRequest(User sender, User receiver, FriendRequestStatus status) {
		this.sender = sender;
		this.receiver = receiver;
		this.status = status;
	}

}
