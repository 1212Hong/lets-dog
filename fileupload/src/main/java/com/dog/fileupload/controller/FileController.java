package com.dog.fileupload.controller;

import com.dog.fileupload.common.api.Api;
import com.dog.fileupload.common.error.ErrorCode;
import com.dog.fileupload.dto.FileResponse;
import com.dog.fileupload.dto.UpdateRequest;
import com.dog.fileupload.entity.FileInfo;
import com.dog.fileupload.service.FileStorageService;
import com.dog.fileupload.utils.FileNameUtils;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
public class FileController {

    private final FileStorageService fileStorageService;

    @PostMapping("/upload")
    public Flux<Api<FileResponse>> uploadFile(@RequestPart("files") Flux<FilePart> fileParts) {
        return fileParts.flatMap(filePart -> {
            return Mono.deferContextual(ctx -> {
                Long userId = Long.parseLong(ctx.get("userPk").toString());
                log.info("불러온 userPk : {}", userId);
                return processFileAndResponse(filePart, userId);
            });
        });
    }


    private Mono<Api<FileResponse>> processFileAndResponse(FilePart filePart, Long userId) {
        return fileStorageService
                .save(Mono.just(filePart), userId)
                .log()
                .map(Api::ok);
    }

    @GetMapping("/file/{fileName:.+}")
    public ResponseEntity<Flux<DataBuffer>> getFile(@PathVariable String fileName) {
        Flux<DataBuffer> file = fileStorageService.load(fileName);
        String fileExtension = FileNameUtils.getBaseOrExtension(fileName, FileNameUtils.EXTENSION);
        MediaType mediaType = FileNameUtils.getMediaTypeForFileName(fileExtension);
        log.info("mediaType : {}", mediaType);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"")
                .contentType(mediaType)
                .body(file);
    }

    @DeleteMapping("/file/{filePk}")
    public Mono<?> deleteFile(@PathVariable Long filePk) {
        return fileStorageService.deleteFile(filePk);
    }

    @PutMapping("/files")
    public Mono<Api<FileResponse>> updateArticlePk(@RequestBody UpdateRequest request) {
        return fileStorageService.updateArticlePk(request);
    }
}
