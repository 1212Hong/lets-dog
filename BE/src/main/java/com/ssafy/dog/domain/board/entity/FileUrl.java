package com.ssafy.dog.domain.board.entity;

import static com.ssafy.dog.domain.board.enums.FileStatus.*;

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

import com.ssafy.dog.domain.board.enums.FileStatus;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "file_url")
public class FileUrl {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long fileUrlId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "board_id")
	private Board board;

	private String fileUrl;

	@Enumerated(EnumType.STRING)
	private FileStatus fileStatus;

	@Builder
	public FileUrl(Board boardId, String fileUrl) {
		this.board = boardId;
		this.fileUrl = fileUrl;
		this.fileStatus = USE;
	}

	// 연관 관계 메서드
	public void setFileUrlList(Board board) {
		this.board = board;
		board.getFileUrlLists().add(this);
	}

	public void removeFile() {
		this.fileStatus = DELETE;
	}

}
