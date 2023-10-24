package com.ssafy.dog.domain.user.entity;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Entity
@Getter
@RequiredArgsConstructor
@Table(name = "User")
public class User {

	@Id
	@Column(name = "user_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userId;
	@Column(unique = true)
	@NonNull
	private String userLoginId;
	@NonNull
	private String userPw;
	@NonNull
	private String userNickname;
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

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Follow> follows;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Dog> dogs;

	// @OneToMany(mappedBy = "user")
	// private List<Board> boards;

	@Builder
	public User(
		String userLoginId, String userPw, String userNickname, String userPicture, LocalDateTime userCreatedAt,
		LocalDateTime userUpdatedAt, String userAboutMe, String userGender, Boolean userTermsAgreed) {
		this.userLoginId = userLoginId;
		this.userPw = userPw;
		this.userNickname = userNickname;
		this.userPicture = userPicture;
		this.userCreatedAt = userCreatedAt;
		this.userUpdatedAt = userUpdatedAt;
		this.userAboutMe = userAboutMe;
		this.userGender = userGender;
		this.userTermsAgreed = userTermsAgreed;
	}
}
