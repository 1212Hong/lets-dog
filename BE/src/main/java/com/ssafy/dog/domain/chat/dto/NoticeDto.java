package com.ssafy.dog.domain.chat.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NoticeDto implements Serializable {

	@NotNull
	private Long roomId;

	@NotNull
	private Long userId;

}
