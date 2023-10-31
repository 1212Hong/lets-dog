package com.ssafy.dog.domain.user.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.ssafy.dog.domain.user.entity.User;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserDto {
	@NotBlank(message = "아이디는 필수 입력 값입니다.")
	@Email(message = "로그인 아이디는 이메일 형식이어야 합니다.")
	private String userLoginId;

	@NotBlank
	@Pattern(regexp = "^(?=.*[A-Z])(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,16}$",
		message = "대문자 하나 이상, 특수문자 하나 이상, 숫자 하나 이상을 포함한 최소 8자, 최대 16자")
	private String userPw;

	@NotBlank
	@Size(min = 2, max = 15)
	private String userNickname;

	@NotNull
	@Size(min = 10, max = 11)
	private String userPhone;

	private String userPicture;

	private String userAboutMe;

	private String userGender;

	@NotNull(message = "약관 동의 여부를 입력해주세요.")
	private Boolean userTermsAgreed;

	public User toEntity() {
		return User.builder()
			.userLoginId(userLoginId)
			.userPw(userPw)
			.userNickname(userNickname)
			.userPhone(userPhone)
			.userPicture(userPicture)
			.userAboutMe(userAboutMe)
			.userGender(userGender)
			.userTermsAgreed(userTermsAgreed)
			.userIsRemoved(false)
			.build();
	}
}
