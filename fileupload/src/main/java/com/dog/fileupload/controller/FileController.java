package com.dog.fileupload.controller;

import com.dog.fileupload.common.api.Api;
import com.dog.fileupload.dto.FileResponse;
import com.dog.fileupload.entity.FileInfo;
import com.dog.fileupload.service.FileStorageService;
import com.dog.fileupload.utils.FileNameUtils;
import java.util.stream.Stream;
import javax.print.attribute.standard.Media;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
public class FileController {

	private final FileStorageService fileStorageService;

	@PostMapping("/upload")
	public Flux<Api<FileResponse>> uploadFile(@RequestPart("files") Flux<FilePart> fileParts) {
		return fileParts.flatMap(
				filePart -> fileStorageService.save(Mono.just(filePart))
						.log()
						.map(file -> Api.ok(new FileResponse(file)))
		);
	}

	@GetMapping("/files")
	public ResponseEntity<Flux<FileInfo>> getListFiles() {
		Stream<FileInfo> fileInfoStream = fileStorageService.loadAll().map(path -> {
			String fileName = path.getFileName().toString();
			String url = UriComponentsBuilder.newInstance().path("/files/{filename}").buildAndExpand(fileName).toUriString();
			return null;
		});

		Flux<FileInfo> fileInfoFlux = Flux.fromStream(fileInfoStream);

		return ResponseEntity.status(HttpStatus.OK).body(fileInfoFlux);
	}
//
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


	@PostMapping("/test")
	public Mono<Api<FileInfo>> testPostFileInfo(@RequestBody FileInfo fileInfo) {
		return fileStorageService.saveFileInfo(fileInfo)
				.map(Api::ok);
	}

}
