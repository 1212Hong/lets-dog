package com.ssafy.dog.domain.board.entity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ssafy.dog.domain.board.enums.FileStatus;
import com.ssafy.dog.domain.user.entity.User;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "like_collect")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LikeEntity {
	@Id
	@GeneratedValue
	private Long likeId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "board_id")
	private Board board;

	@Enumerated(EnumType.STRING)
	private FileStatus likeStatus;

	@Builder
	public LikeEntity(User user, Board board) {
		this.board = board;
		this.user = user;
		this.likeStatus = FileStatus.USE;
	}

	public void changeLikeStatus() {
		this.likeStatus = FileStatus.DELETE;
	}
}
