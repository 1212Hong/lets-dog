package com.dog.fileupload.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class FileResponse {
    private String url;

	public FileResponse(String url) {
		this.url = url;
	}
}
