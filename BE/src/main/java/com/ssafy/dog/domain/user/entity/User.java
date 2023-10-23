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
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(unique = true)
	@NonNull
	private String loginId;
	@NonNull
	private String pw;
	@NonNull
	private String nickname;
	@Lob
	private String picture;
	@NonNull
	private LocalDateTime createdAt;
	@NonNull
	private LocalDateTime updatedAt;
	private String aboutMe;
	private String gender;
	@NonNull
	private Boolean termsAgreed;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Follow> follows;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Dog> dogs;

	// @OneToMany(mappedBy = "user")
	// private List<Board> boards;

	@Builder
	public User(
		String loginId, String pw, String nickname, String picture, LocalDateTime createdAt,
		LocalDateTime updatedAt, String aboutMe, String gender, Boolean termsAgreed) {
		this.loginId = loginId;
		this.pw = pw;
		this.nickname = nickname;
		this.picture = picture;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.aboutMe = aboutMe;
		this.gender = gender;
		this.termsAgreed = termsAgreed;
	}
}
