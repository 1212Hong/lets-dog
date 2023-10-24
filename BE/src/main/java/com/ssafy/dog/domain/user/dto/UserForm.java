package com.ssafy.dog.domain.user.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Range;

import com.ssafy.dog.domain.user.entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserForm {
	@NotBlank(message = "아이디는 필수 입력 값입니다.")
	@Email(message = "로그인 아이디는 이메일 형식이어야 합니다.")
	private String userLoginId;

	@NotBlank
	@Pattern(regexp = "^(?=.*[A-Z])(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,16}$",
		message = "대문자 하나 이상, 특수문자 하나 이상, 숫자 하나 이상을 포함한 최소 8자, 최대 16자")
	private String userPw;

	@NotBlank
	@Range(min = 2, max = 15)
	private String userNickname;

	private String userPicture;

	private LocalDateTime userCreatedAt;

	private LocalDateTime userUpdatedAt;

	private String userAboutMe;

	private String userGender;

	@NotBlank(message = "약관 동의 여부를 입력해주세요.")
	private Boolean userTermsAgreed;

	@Builder
	public User toEntity() {
		return User.builder()
			.userLoginId(userLoginId)
			.userPw(userPw)
			.userNickname(userNickname)
			.userPicture(userPicture)
			.userCreatedAt(LocalDateTime.now())
			.userUpdatedAt(LocalDateTime.now())
			.userAboutMe(userAboutMe)
			.userGender(userGender)
			.userTermsAgreed(userTermsAgreed)
			.build();
	}
}
