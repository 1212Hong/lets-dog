package com.ssafy.dog.domain.board.entity;

import javax.persistence.Column;
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
import com.ssafy.dog.domain.board.enums.FileStatus;
import com.ssafy.dog.domain.user.entity.User;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "comment")
public class Comment extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "comment_id")
	private Long commentId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "board_id")
	private Board board;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	private String commentContent;
	private int commentLikes;
	@Enumerated(EnumType.STRING)
	private FileStatus commentStatus;

	@Builder
	public Comment(User user, Board board, String commentContent, FileStatus commentStatus) {
		this.commentContent = commentContent;
		this.user = user;
		this.board = board;
		this.commentLikes = 0;
		this.commentStatus = commentStatus;
	}

	// == 연관 관계 메서드 == //
	public void setMember(User user) {
		this.user = user;
		user.getCommentListForUser().add(this);
	}

	public void setBoard(Board board) {
		this.board = board;
		board.getCommentList().add(this);
	}

	public void removeComment() {
		this.commentStatus = FileStatus.DELETE;
	}
}
