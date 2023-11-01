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

	private String boardTitle;

	private String boardContent;

	private int boardLikes;

	@Enumerated(value = EnumType.STRING)
	private Scope boardScope;

	@OneToMany(mappedBy = "board")
	private List<Comment> commentListForBoard = new ArrayList<>();

	@OneToMany(mappedBy = "board")
	private List<FileUrl> fileUrlLists = new ArrayList<>();

	@Builder
	public Board(User user, String boardTitle, String boardContent, Scope boardScope) {
		this.user = user;
		this.boardTitle = boardTitle;
		this.boardContent = boardContent;
		this.boardScope = boardScope;
		this.boardLikes = 0;
	}

	// === 연관 관계 메서드 === //
	public void setMemberFromBoard(User user) {
		this.user = user;
		user.getBoardList().add(this);
	}

}
