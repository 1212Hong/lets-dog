package com.ssafy.dog.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

/*
사용 예시 필요시 에러코드 생성해 사용
유저 서비스 에러는 1000번대
 */
@AllArgsConstructor
@Getter
public enum UserErrorCode implements ErrorCodeIfs {
    // 회원가입 관련
    EMAIL_EXISTS(400, 1404, "해당 이메일은 이미 사용 중입니다."),
    INVALID_PASSWORD(400, 1405, "형식에 맞지 않는 비밀번호 입니다."),
    NICKNAME_EXISTS(400, 1406, "해당 닉네임은 이미 사용 중입니다."),
    TERMS_NOT_AGREED(400, 1407, "약관 동의를 하지 않았거나, 해당 값이 null 입니다."),
    PHONE_EXISTS(400, 1408, "이미 가입한 핸드폰 번호입니다."),
    INVALID_EMAIL(400, 1409, "형식에 맞지 않는 이메일 입니다."),

    // 로그인 관련
    USER_NOT_FOUND(404, 1410, "사용자를 찾을 수 없음."),
    WRONG_PASSWORD(401, 1411, "유저는 존재하나 잘못된 비밀번호입니다."),
    // INVALID_PASSWORD(400, 1405, "형식에 맞지 않는 비밀번호 입니다."),
    // INVALID_EMAIL(400, 1409, "형식에 맞지 않는 이메일 입니다."),

    // 친구 관련
    SENDER_NOT_FOUND(404, 1412, "친구 요청 sender 를 찾을 수 없습니다."),
    RECEIVER_NOT_FOUND(404, 1413, "친구 요청 receiver 를 찾을 수 없습니다."),
    I_AM_MY_OWN_FRIEND(400, 1414, "자기 자신을 친구 추가 요청 할 수 없습니다."),
    REQUEST_ALREADY_SENT(409, 1415, "본인이 친구 요청을 이미 보냈습니다."),
    ALREADY_FRIENDS(409, 1416, "본인이 이미 친구를 맺었습니다."),
    PREVIOUSLY_DECLINED(409, 1417, "본인이 이미 거절을 당했습니다."), // 거절 당하는 주체가 누군지는 테스트 해봐야 알듯
    WRONG_STATUS_TYPE(400, 1418, "ENUM 에 없는 status 입니다."),
    THEIR_REQUEST_ALREADY_SENT(409, 1419, "상대방이 친구 요청을 이미 보냈습니다."),
    THEIR_ALREADY_FRIENDS(409, 1420, "상대방이 이미 친구를 맺었습니다."),
    THEIR_PREVIOUSLY_DECLINED(409, 1421, "상대방이 이미 거절을 당했습니다."),
    THEIR_WRONG_STATUS_TYPE(400, 1422, "상대방이 ENUM 에 없는 status 입니다."),
    REQUEST_NOT_FOUND(404, 1423, "신청자가 보낸 친구 요청을 찾을 수 없음."),
    CANNOT_DECLINE_PROCESSED_REQUEST(409, 1424, "이미 수락되었거나 거절한 요청에 대해서는 처리할 수 없음."),
    NOT_FRIENDS(409, 1425, "서로 친구가 아닙니다.");

    private final Integer httpStatusCode;
    private final Integer errorCode;
    private final String description;

}
