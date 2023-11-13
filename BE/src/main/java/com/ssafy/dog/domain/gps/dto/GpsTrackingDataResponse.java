package com.ssafy.dog.domain.gps.dto;

import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GpsTrackingDataResponse {
	private List<GpsTrackingResponse> contents;
	private int totalPages;
	private long totalElements;

	public GpsTrackingDataResponse(List<GpsTrackingResponse> contents, int totalPages, long totalElements) {
		this.contents = contents;
		this.totalPages = totalPages;
		this.totalElements = totalElements;
	}
}
