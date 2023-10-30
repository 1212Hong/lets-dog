package com.dog.fileupload.service;

import com.dog.fileupload.common.api.Api;
import com.dog.fileupload.common.error.ErrorCode;
import com.dog.fileupload.common.exception.ApiException;
import com.dog.fileupload.dto.FileResponse;
import com.dog.fileupload.dto.UpdateRequest;
import com.dog.fileupload.entity.FileInfo;
import com.dog.fileupload.enums.FileStatus;
import com.dog.fileupload.enums.FileType;
import com.dog.fileupload.repository.FileInfoRepository;
import com.dog.fileupload.utils.EncodeFile;
import com.dog.fileupload.utils.FileNameUtils;
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
    @Value("${file.downloadApi}")
    private String downloadApi;
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
    public Mono<FileResponse> save(Mono<FilePart> filePartMono, Long userPk) {
        return filePartMono
                .doOnNext(fp -> log.info("Receiving File: {}", fp.filename()))
                .flatMap(fp -> saveFileAndStoreMetadata(fp, userPk));
    }

    private Mono<FileResponse> saveFileAndStoreMetadata(FilePart filePart, Long userPk) {
        FileInfo.FileInfoBuilder fileInfoBuilder = FileInfo.builder()
                .userId(userPk)
                .originalName(filePart.filename());
        return saveAndEncodeFile(filePart, fileInfoBuilder)
                .flatMap(encodedFileName -> {
                    fileInfoBuilder.encodedName(encodedFileName);
                    fileInfoBuilder.url(downloadApi + encodedFileName);
                    fileInfoBuilder.fileStatus(FileStatus.COMPLETED);
                    return saveMetadata(fileInfoBuilder.build());
                });
    }

    private Mono<FileResponse> saveMetadata(FileInfo fileInfo) {
        return fileInfoRepository.save(fileInfo)
                .doOnSuccess(i -> log.info("Metadata 저장 : {}", i))
                .doOnError(e -> log.error("Metadata 저장 실패: {}", e))
                .map(FileResponse::toResponse);
    }

    private Mono<String> saveAndEncodeFile(FilePart filePart, FileInfo.FileInfoBuilder fileInfoBuilder) {
        String filename = FileNameUtils.convert(filePart.filename());
        Path originalFilePath = root.resolve(filename);
        String mimeType = filePart.headers().getContentType().toString();
        if (mimeType.startsWith("video/")) {
            fileInfoBuilder.fileType(FileType.VIDEO);
        } else if (mimeType.startsWith("image/")) {
            fileInfoBuilder.fileType(FileType.IMAGE);
        }
        return transferFile(filePart, originalFilePath)
                .flatMap(path -> encodeAndDeleteOriginal(path, mimeType)
                        .publishOn(Schedulers.boundedElastic()));
    }

    private Mono<Path> transferFile(FilePart filePart, Path path) {
        return filePart.transferTo(path).thenReturn(path);
    }

    private Mono<String> encodeAndDeleteOriginal(Path originalFilePath, String mimeType) {
        return Mono.fromCallable(() -> {
            String encodedFileName;
            if (mimeType.startsWith("video/")) {
                encodedFileName = encodeFile.encodeVideo(originalFilePath.toString());
            } else if (mimeType.startsWith("image/")) {
                encodedFileName = encodeFile.encodeImage(originalFilePath.toString());
            } else {
                throw new ApiException(ErrorCode.BAD_REQUEST, "Unsupported file type");
            }

            deleteOriginalFile(originalFilePath);

            return encodedFileName;
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
        return fileInfoRepository.findByEncodedName(filename)
                .flatMap(info -> {
                    if (info.getFileStatus() == FileStatus.DELETED) {
                        return Mono.error(new ApiException(ErrorCode.BAD_REQUEST, "삭제된 파일입니다."));
                    }
                    try {
                        Path file = root.resolve(filename);
                        Resource resource = new UrlResource(file.toUri());
                        if (resource.exists() || resource.isReadable()) {
                            return Mono.just(resource);
                        } else {
                            return Mono.error(new ApiException(ErrorCode.SERVER_ERROR, "파일을 읽을 수 없습니다."));
                        }
                    } catch (MalformedURLException e) {
                        return Mono.error(new ApiException(ErrorCode.SERVER_ERROR, "Error: " + e.getMessage()));
                    }
                })
                .flatMapMany(resource -> DataBufferUtils.read(resource, new DefaultDataBufferFactory(), 4096))
                .switchIfEmpty(Flux.error(new ApiException(ErrorCode.BAD_REQUEST, "존재하지 않는 파일입니다.")));
    }


    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.root, 1)
                    .filter(path -> !path.equals(this.root))
                    .map(this.root::relativize);
        } catch (IOException e) {
            throw new ApiException(ErrorCode.BAD_REQUEST, "파일을 찾을 수 없습니다.");
        }
    }


    @Override
    public Mono<FileInfo> saveFileInfo(FileInfo fileInfo) {
        return fileInfoRepository.save(fileInfo)
                .doOnSuccess(i -> log.info("저장완료 : {}", i))
                .doOnError(e -> log.error("저장 실패", e))
                .doOnTerminate(() -> log.info("저장 작업 종료"));
    }

    @Override
    public Mono<Api<FileResponse>> deleteFile(Long filePk) {
        // TODO : 일정 기간이 지나면 deleted 상태의 파일 물리적 삭제
        return fileInfoRepository.findById(filePk)
                .switchIfEmpty(Mono.error(new ApiException(ErrorCode.BAD_REQUEST, "존재하지 않는 파일입니다.")))
                .flatMap(existingFileInfo -> {
                    if (existingFileInfo.getFileStatus() == FileStatus.DELETED) {
                        return Mono.error(new ApiException(ErrorCode.BAD_REQUEST, "파일은 이미 삭제된 상태입니다."));
                    }
                    existingFileInfo.deleteFile(FileStatus.DELETED);
                    return fileInfoRepository.save(existingFileInfo);
                }).map(FileResponse::toResponse)
                .map(Api::ok)
                .doOnSuccess(info -> log.info("FilePk : {} 삭제", filePk))
                .doOnError(e -> log.error("{} 파일 삭제시 {} 에러 발생", filePk, e.getMessage()));
    }

    @Override
    public Mono<Api<FileResponse>> updateArticlePk(UpdateRequest request) {
        return fileInfoRepository.findById(request.getFilePk())
                .switchIfEmpty(Mono.error(new ApiException(ErrorCode.BAD_REQUEST, "존재하지 않는 파일입니다.")))
                .flatMap(it -> {
                    it.changeArticlePk(request.getArticlePk());
                    return fileInfoRepository.save(it);
                })
                .map(FileResponse::toResponse)
                .map(Api::ok);
    }
}


