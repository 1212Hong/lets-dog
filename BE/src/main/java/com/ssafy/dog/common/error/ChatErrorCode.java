package com.ssafy.dog.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

/*
사용 예시 필요시 에러코드 생성해 사용
유저 서비스 에러는 1000번대
 */
@AllArgsConstructor
@Getter
public enum ChatErrorCode implements ErrorCodeIfs {
	/*
	200 OK - 요청 성공
	201 Created - 요청에 따른 새로운 리소스 생성 성공
	204 No Content - 요청은 성공했지만 딱히 보내줄 내용이 없음
	400 Bad Request - 잘못된 요청
	401 Unauthorized - 비인증 요청
	403 Forbidden - 비승인 요청
	404 Not Found - 존재하지 않는 리소스에 대한 요청
	500 Internal Server Error - 서버 에러
	503 Service Unavailable - 서비스가 이용 불가능함
	 */
	CHATROOM_NOT_FOUND(404, 2404, "해당 채팅방이 존재하지 않습니다"),
	CHATROOM_USER_NOT_SELECT(400, 2404, "채팅을 시작할 유저가 선택되지 않았습니다"),

	ROOM_USER_NOT_FOUND(404, 2404, "채팅방의 접속자가 아닙니다"),
	JWT_NOT_FOUND(403, 2401, "JWT 토큰이 없습니다");

	private final Integer httpStatusCode;
	private final Integer errorCode;
	private final String description;

}
