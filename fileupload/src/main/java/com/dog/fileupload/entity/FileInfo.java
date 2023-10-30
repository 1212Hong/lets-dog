package com.dog.fileupload.entity;


import com.dog.fileupload.enums.FileStatus;
import com.dog.fileupload.enums.FileType;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;


@Getter
@NoArgsConstructor
@ToString
public class FileInfo {
    @Id
    private Long id;
    private Long userId;
    private Long articleId;
    private String originalName;
    private String encodedName;
    private FileType fileType;
    private FileStatus fileStatus;
    private String url;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Builder
    private FileInfo(Long userId, Long articleId, String originalName, String encodedName, FileType fileType,
                     FileStatus fileStatus,
                     String url) {
        this.userId = userId;
        this.articleId = articleId;
        this.originalName = originalName;
        this.encodedName = encodedName;
        this.fileType = fileType;
        this.fileStatus = fileStatus;
        this.url = url;
    }

    public void changeEncodedFileName(String encodedName) {
        this.encodedName = encodedName;
    }

    public void deleteFile(FileStatus fileStatus) {
        this.fileStatus = fileStatus;
    }

    public void changeArticlePk(Long articleId) {
        this.articleId = articleId;
    }
}
