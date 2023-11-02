package com.ssafy.dog.domain.user.dto.response;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserLoginResponseDto {

	@Email(message = "로그인 아이디는 이메일 형식이어야 합니다.",
		regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")
	private String userLoginId;

	@Min(value = 2, message = "닉네임은 2글자 이상, 15글자 이하여야 합니다.")
	@Max(value = 15, message = "닉네임은 2글자 이상, 15글자 이하여야 합니다.")
	private String userNickname;

	private String userPicture;

	private String jwt;

	public UserLoginResponseDto(String userLoginId, String userNickname, String userPicture, String jwt) {
		this.userLoginId = userLoginId;
		this.userNickname = userNickname;
		this.userPicture = userPicture;
		this.jwt = jwt;
	}
}
