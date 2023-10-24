package com.ssafy.dog.domain.user.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
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

	// @Builder
	// public User(
	// 	String userLoginId, String userPw, String userNickname, String userPicture, LocalDateTime userCreatedAt,
	// 	LocalDateTime userUpdatedAt, String userAboutMe, String userGender, Boolean userTermsAgreed) {
	// 	this.userLoginId = userLoginId;
	// 	this.userPw = userPw;
	// 	this.userNickname = userNickname;
	// 	this.userPicture = userPicture;
	// 	this.userCreatedAt = userCreatedAt;
	// 	this.userUpdatedAt = userUpdatedAt;
	// 	this.userAboutMe = userAboutMe;
	// 	this.userGender = userGender;
	// 	this.userTermsAgreed = userTermsAgreed;
	// }
}
