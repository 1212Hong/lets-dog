package com.dog.fileupload.service;

import com.dog.fileupload.dto.FileResponse;
import com.dog.fileupload.entity.FileInfo;
import java.nio.file.Path;
import java.util.stream.Stream;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FileStorageService {

    public void init();

    public Mono<FileResponse> save(Mono<FilePart> filePartMono, Long userPk);

    public Flux<DataBuffer> load(String filename);

    public Stream<Path> loadAll();

    public Mono<FileInfo> saveFileInfo(FileInfo info);


}
