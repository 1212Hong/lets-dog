package com.ssafy.dog.domain.chat.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.ssafy.dog.common.auditing.BaseTimeEntity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ChatRoom extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long roomId;

	private String roomTitle;

	@OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL)
	private List<ChatMembers> chatMembers;

	@Builder
	public ChatRoom(Long roomId, String roomTitle, List<ChatMembers> chatMembers) {
		this.roomId = roomId;
		this.roomTitle = roomTitle;
		this.chatMembers = chatMembers;
	}

	public Long getRoomId() {
		return this.roomId;
	}

}
