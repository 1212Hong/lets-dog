package com.dog.fileupload.repository;

import com.dog.fileupload.entity.FileInfo;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface FileInfoRepository extends R2dbcRepository<FileInfo, Long> {

}
