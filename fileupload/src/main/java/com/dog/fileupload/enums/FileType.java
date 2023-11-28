package com.dog.fileupload.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum FileType {
    IMAGE("사진"),
    VIDEO("영상");

    private final String description;
}
