package com.dog.fileupload.service;

import com.dog.fileupload.common.error.ErrorCode;
import com.dog.fileupload.common.exception.ApiException;
import com.dog.fileupload.entity.FileInfo;
import com.dog.fileupload.repository.FileInfoRepository;
import com.dog.fileupload.utils.EncodeFile;
import com.dog.fileupload.utils.FileNameConverter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileStorageServiceImpl implements FileStorageService {

    @Value("${file.uploadUrl}")
    private String uploadUrl;
    private final FileInfoRepository fileInfoRepository;
    private final EncodeFile encodeFile;
    private final Path root = Paths.get("uploads");

    @Override
    public void init() {
        try {
            Files.createDirectories(root);
        } catch (IOException e) {
            throw new ApiException(ErrorCode.SERVER_ERROR, "업로드 폴더를 생성할 수 없습니다.");
        }
    }

    @Override
    public Mono<String> save(Mono<FilePart> filePartMono) {
        return filePartMono
                .doOnNext(fp -> log.info("Receiving File: {}", fp.filename()))
                .flatMap(this::saveAndEncodeFile);
    }

    private Mono<String> saveAndEncodeFile(FilePart filePart) {
        String filename = FileNameConverter.convert(filePart.filename());
        Path originalFilePath = root.resolve(filename);
        String mimeType = filePart.headers().getContentType().toString();
        return transferFile(filePart, originalFilePath)
                .flatMap(path -> encodeAndDeleteOriginal(path, mimeType)
                        .publishOn(Schedulers.boundedElastic()));
    }

    private Mono<Path> transferFile(FilePart filePart, Path path) {
        return filePart.transferTo(path).thenReturn(path);
    }

    private Mono<String> encodeAndDeleteOriginal(Path originalFilePath, String mimeType) {
        return Mono.fromCallable(() -> {
            String encodedFilePath;
            if (mimeType.startsWith("video/")) {
                encodedFilePath = encodeFile.encodeVideo(originalFilePath.toString());
            } else if (mimeType.startsWith("image/")) {
                encodedFilePath = encodeFile.encodeImage(originalFilePath.toString());
            } else {
                throw new ApiException(ErrorCode.BAD_REQUEST, "Unsupported file type");
            }

            deleteOriginalFile(originalFilePath);

            return uploadUrl + encodedFilePath;
        }).subscribeOn(Schedulers.boundedElastic());
    }

    private void deleteOriginalFile(Path path) {
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new ApiException(ErrorCode.SERVER_ERROR, e, "파일을 삭제할 수 없습니다.");
        }
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


