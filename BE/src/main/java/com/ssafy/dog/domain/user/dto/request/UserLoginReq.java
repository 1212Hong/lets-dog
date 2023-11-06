package com.ssafy.dog.domain.user.dto.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import lombok.Getter;

@Getter
public class UserLoginReq {
	@NotBlank(message = "이메일(아이디)은 필수 입력 값입니다.")
	@Email(message = "로그인 아이디는 이메일 형식이어야 합니다.",
		regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")
	private String userLoginId;

	@NotBlank
	@Pattern(regexp = "^(?=.*[A-Z])(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,16}$",
		message = "대문자 하나 이상, 특수문자 하나 이상, 숫자 하나 이상을 포함한 최소 8자, 최대 16자")
	private String userPw;
}
