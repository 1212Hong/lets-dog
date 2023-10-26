package com.dog.fileupload.entity;


import com.dog.fileupload.enums.FileStatus;
import com.dog.fileupload.enums.FileType;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Table;


@Getter
@NoArgsConstructor
@ToString
public class FileInfo {
    @Id
    private Long id;
    private Long userId;
    private Long articleId;
    private String originalName;
    private String uuidName;
    private FileType fileType;
    private FileStatus fileStatus;
    private String url;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Builder
    public FileInfo(Long userId, Long articleId, String originalName, String uuidName, FileType fileType,
                    FileStatus fileStatus,
                    String url) {
        this.userId = userId;
        this.articleId = articleId;
        this.originalName = originalName;
        this.uuidName = uuidName;
        this.fileType = fileType;
        this.fileStatus = fileStatus;
        this.url = url;
    }
}
