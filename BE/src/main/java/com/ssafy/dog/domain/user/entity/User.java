package com.ssafy.dog.domain.user.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

import com.ssafy.dog.domain.board.entity.Board;
import com.ssafy.dog.domain.board.entity.Comment;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user")
public class User {

	@Id
	@Column(name = "user_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userId;

	@Column(unique = true)
	@Email
	@NonNull
	private String userLoginId;

	@NonNull
	private String userPw;

	@Column(unique = true)
	@NonNull
	private String userNickname;

	@NonNull
	@Column(name = "user_phone")
	@Size(max = 11)
	private String userPhone;

	@Lob
	private String userPicture;

	@NonNull
	private LocalDateTime userCreatedAt;

	@NonNull
	private LocalDateTime userUpdatedAt;

	private String userAboutMe;

	private String userGender;

	@NonNull
	private Boolean userTermsAgreed;

	@NonNull
	private Boolean userIsRemoved;

	@Builder
	public User( // 빌더는 UserForm 에도 있어야 하고 여기 User 엔티티에도 있어야 하나?
		String userLoginId, String userPw, String userNickname, String userPhone, String userPicture,
		LocalDateTime userCreatedAt,
		LocalDateTime userUpdatedAt, String userAboutMe, String userGender, Boolean userTermsAgreed,
		Boolean userIsRemoved) {
		this.userLoginId = userLoginId;
		this.userPw = userPw;
		this.userNickname = userNickname;
		this.userPhone = userPhone;
		this.userPicture = userPicture;
		this.userCreatedAt = userCreatedAt;
		this.userUpdatedAt = userUpdatedAt;
		this.userAboutMe = userAboutMe;
		this.userGender = userGender;
		this.userTermsAgreed = userTermsAgreed;
		this.userIsRemoved = userIsRemoved;
	}

	// === 연결 === //
	@OneToMany(mappedBy = "user")
	private List<Board> boardList = new ArrayList<>();

	@OneToMany(mappedBy = "user")
	private List<Comment> commentListForUser = new ArrayList<>();
}
