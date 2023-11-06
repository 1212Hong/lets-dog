package com.ssafy.dog.domain.board.entity;

import static javax.persistence.FetchType.*;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.ssafy.dog.common.auditing.BaseTimeEntity;
import com.ssafy.dog.domain.board.enums.FileStatus;
import com.ssafy.dog.domain.board.enums.Scope;
import com.ssafy.dog.domain.user.entity.User;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "board")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "board_id")
	private Long boardId;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	private String boardContent;

	private int boardLikes;

	@Enumerated(value = EnumType.STRING)
	private Scope boardScope;

	@OneToMany(mappedBy = "board")
	private List<Comment> commentList = new ArrayList<>();

	@OneToMany(mappedBy = "board")
	private List<FileUrl> fileUrlLists = new ArrayList<>();

	@Enumerated(EnumType.STRING)
	private FileStatus boardStatus;

	private int boardComments;

	@Builder
	public Board(User user, String boardContent, Scope boardScope, FileStatus boardStatus) {
		this.user = user;
		this.boardContent = boardContent;
		this.boardScope = boardScope;
		this.boardLikes = 0;
		this.boardStatus = boardStatus;
		this.boardComments = 0;
	}

	// === 연관 관계 메서드 === //
	public void setMemberFromBoard(User user) {
		this.user = user;
		user.getBoardList().add(this);
	}

	public void removeBoard() {
		this.boardStatus = FileStatus.DELETE;
	}

	public void increaseBoardLikes() {
		this.boardLikes += 1;
	}

	public void decreaseBoardLikes() {
		this.boardLikes -= 1;
	}

	public void increaseBoardComment() {
		this.boardComments += 1;
	}

	public void decreaseBoardComment() {
		this.boardComments -= 1;
	}

}
