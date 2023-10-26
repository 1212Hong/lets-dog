package com.dog.fileupload.service;

import com.dog.fileupload.entity.FileInfo;
import com.dog.fileupload.enums.FileStatus;
import com.dog.fileupload.enums.FileType;
import com.dog.fileupload.repository.FileInfoRepository;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileStorageServiceImpl implements FileStorageService {
    private final FileInfoRepository fileInfoRepository;
    private final Path root = Paths.get("uploads");

    @Override
    public void init() {
        try {
            Files.createDirectories(root);
        } catch (IOException e) {
            throw new RuntimeException("업로드 폴더를 생성할 수 없습니다.");
        }
    }

    @Override
    public Mono<String> save(Mono<FilePart> filePartMono) {

        return filePartMono.doOnNext(fp -> log.info("Receiving File: {}", fp.filename())).flatMap(filePart -> {
            String filename = filePart.filename();
            return filePart.transferTo(root.resolve(filename)).then(Mono.just(filename));
        });
    }

    @Override
    public Flux<DataBuffer> load(String filename) {
        try {
            Path file = root.resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return DataBufferUtils.read(resource, new DefaultDataBufferFactory(), 4096);
            } else {
                throw new RuntimeException("파일을 읽을 수 없습니다.");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.root, 1)
                    .filter(path -> !path.equals(this.root))
                    .map(this.root::relativize);
        } catch (IOException e) {
            throw new RuntimeException("피을을 읽을 수 없습니다.");
        }
    }

    @Override
    public Mono<FileInfo> saveFileInfo(FileInfo fileInfo) {

        return fileInfoRepository.save(fileInfo)
                .doOnSuccess(i -> log.info("저장완료 : {}", i))
                .doOnError(e -> log.error("저장 실패", e))
                .doOnTerminate(() -> log.info("저장 작업 종료"));
    }
}


