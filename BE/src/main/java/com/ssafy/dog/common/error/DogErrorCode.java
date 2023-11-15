package com.ssafy.dog.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DogErrorCode implements ErrorCodeIfs {
    // Dog Create
    USER_NOT_FOUND(404, 1601, "유저가 존재하지 않습니다."),
    NOT_UNIQUE_ELEMENTS(400, 1602, "dog_disposition_list 의 값들이 겹치지 않아야 합니다."),
    DOG_NOT_FOUND(404, 1603, "dogId 로 찾은 개가 존재하지 않습니다."),
    NOT_DOG_OWNER(404, 1604, "유저가 개의 주인이 아닙니다."),
    NAME_SIZE_ERROR(400, 1605, "개의 이름은 10자 이하여야 합니다."),
    BREED_SIZE_ERROR(400, 1606, "개 품종의 이름은 20자 이하여야 합니다."),
    DISPOSITION_LIST_SIZE_ERROR(400, 1607, "개 성향 정보는 3개 이상 5개 이하여야 합니다."),
    ABOUTME_SIZE_ERROR(400, 1608, "개 자기소개는 200자 이하여야 합니다."),
    INVALID_DOG_UPDATE_FIELDS(400, 1609, "유효하지 않은 개 속성입니다."),
    DISPOSITION_VALUE_ERROR(400, 1610, "입력된 개 성향이 DogDisposition 내의 값들과 다릅니다."),
    SIZE_VALUE_ERROR(400, 1611, "입력된 개 크기가 'SMALL', 'MEDIUM', 'LARGE' 중 하나가 아닙니다."),
    DISPOSITION_DUPLICATE_ERROR(400, 1612, "입력된 개 성향이 중복됩니다."),
    ;
    // 프론트와 협의 후 에러 탐지 로직 추가 예정

    private final Integer httpStatusCode;
    private final Integer errorCode;
    private final String description;

}
