package com.ssafy.dog.domain.gps.dto;

import java.time.LocalDateTime;

import com.ssafy.dog.domain.gps.entity.GpsPoints;
import com.ssafy.dog.domain.gps.entity.GpsTracking;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class GpsTrackingResponse {

	private LocalDateTime trackingDate;
	private LocalDateTime createdDate;
	private LocalDateTime modifiedDate;
	private GpsPoints gpsPoints;

	@Builder
	public GpsTrackingResponse(LocalDateTime trackingDate, LocalDateTime createdDate, LocalDateTime modifiedDate,
		GpsPoints gpsPoints) {
		this.trackingDate = trackingDate;
		this.gpsPoints = gpsPoints;
		this.createdDate = createdDate;
		this.modifiedDate = modifiedDate;
	}

	public static GpsTrackingResponse toResponse(GpsTracking gpsTracking) {
		return GpsTrackingResponse.builder()
			.gpsPoints(gpsTracking.getGpsPoints())
			.trackingDate(gpsTracking.getTrackingDate())
			.createdDate(gpsTracking.getCreatedDate())
			.modifiedDate(gpsTracking.getModifiedDate())
			.build();
	}
}
