package com.dog.fileupload.repository;

import com.dog.fileupload.entity.FileInfo;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface FileInfoRepository extends R2dbcRepository<FileInfo, Long> {
    Mono<FileInfo> findByEncodedName(String encodedName);

    Mono<FileInfo> findByUrl(String url);
}
