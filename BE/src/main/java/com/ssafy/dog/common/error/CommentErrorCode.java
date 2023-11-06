package com.ssafy.dog.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CommentErrorCode implements ErrorCodeIfs {
	COMMENT_NOT_FOUND(400, 1601, "댓글 내용은 필수로 작성해야합니다"),
	COMMENT_TOO_LONG(400, 1602, "댓글은 50자를 넘을 수 없습니다.");

	private final Integer httpStatusCode;
	private final Integer errorCode;
	private final String description;
}
