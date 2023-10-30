package com.dog.fileupload.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UpdateRequest {
    private Long articlePk;
    private Long filePk;
}
