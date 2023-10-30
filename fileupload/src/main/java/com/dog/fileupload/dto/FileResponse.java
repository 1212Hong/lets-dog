package com.dog.fileupload.dto;

import com.dog.fileupload.entity.FileInfo;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class FileResponse {

    private Long filePk;
    private String url;

    @Builder
    private FileResponse(String url, Long filePk) {
        this.filePk = filePk;
        this.url = url;
    }

    public static FileResponse toResponse(FileInfo fileInfo) {
        return FileResponse.builder()
                .filePk(fileInfo.getId())
                .url(fileInfo.getUrl())
                .build();
    }
}
