package com.ssafy.dog.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EmailErrorCode implements ErrorCodeIfs{
    UNABLE_TO_SEND_EMAIL(500, 1701, "이메일을 보낼 수 없습니다.");

    private final Integer httpStatusCode;
    private final Integer errorCode;
    private final String description;
}
