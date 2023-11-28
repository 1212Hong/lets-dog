package com.ssafy.dog.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BoardErrorCode implements ErrorCodeIfs {

	TITLE_NOT_FOUND(400, 1501, "게시글 제목은 필수로 작성해야합니다"),
	TITLE_TOO_LONG(400, 1502, "게시글 제목은 50자를 초과할 수 없습니다"),
	CONTENT_NOT_FOUND(400, 1503, "게시글 내용은 필수로 작성해야합니다"),
	CONTENT_TOO_LONG(400, 1504, "게시글 내용은 200자를 초과할 수 없습니다."),

	BOARD_LIST_IS_EMPTY(400, 1504, "게시글이 존재하지 않습니다"),
	COMMENT_NOT_FOUND(400, 1505, "존재하지 않는 댓글입니다"),
	USER_NOT_MATCH(400, 1506, "작성자가 일치하지 않습니다");

	private final Integer httpStatusCode;
	private final Integer errorCode;
	private final String description;
}
