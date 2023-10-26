package com.dog.fileupload.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum FileStatus {

    PENDING("업로드중"),
    COMPLETED("업로드 완료"),
    DELETED("삭제");

    private final String description;

}
