package com.dog.fileupload.service;

import com.dog.fileupload.common.api.Api;
import com.dog.fileupload.dto.FileResponse;
import com.dog.fileupload.dto.UpdateRequest;
import com.dog.fileupload.entity.FileInfo;
import java.nio.file.Path;
import java.util.stream.Stream;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FileStorageService {

    void init();

    Mono<FileResponse> save(Mono<FilePart> filePartMono, Long userPk);

    Flux<DataBuffer> load(String filename);

    Stream<Path> loadAll();

    Mono<FileInfo> saveFileInfo(FileInfo info);

    Mono<?> deleteFile(Long filePk);

    Mono<Api<FileResponse>> updateArticlePk(UpdateRequest request);

}
