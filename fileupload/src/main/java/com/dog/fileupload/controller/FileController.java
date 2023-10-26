package com.dog.fileupload.controller;

import com.dog.fileupload.common.api.Api;
import com.dog.fileupload.dto.FileResponse;
import com.dog.fileupload.entity.FileInfo;
import com.dog.fileupload.service.FileStorageService;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class FileController {

	private final FileStorageService fileStorageService;

	@PostMapping("/upload")
	public Mono<ResponseEntity<FileResponse>> uploadFile(@RequestPart("file") Mono<FilePart> filePartMono) {
		return fileStorageService.save(filePartMono).map(
			(fileName) -> ResponseEntity.ok().body(new FileResponse("업로드 성공 : " + fileName))
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

	@GetMapping("/file/{filename:.+}")
	public ResponseEntity<Flux<DataBuffer>> getFile(@PathVariable String fileName) {
		Flux<DataBuffer> file = fileStorageService.load(fileName);

		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
			.contentType(MediaType.APPLICATION_OCTET_STREAM).body(file);
	}

	@PostMapping("/test")
	public Mono<Api<FileInfo>> testPostFileInfo(@RequestBody FileInfo fileInfo) {
		return fileStorageService.saveFileInfo(fileInfo)
				.map(Api::ok);
	}

}
