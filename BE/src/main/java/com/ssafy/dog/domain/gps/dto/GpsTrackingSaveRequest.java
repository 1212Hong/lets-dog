package com.ssafy.dog.domain.gps.dto;

import java.time.LocalDateTime;

import com.ssafy.dog.domain.gps.entity.GpsPoints;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class GpsTrackingSaveRequest {

	private LocalDateTime trackingDate;
	private GpsPoints gpsPoints;

	@Builder
	public GpsTrackingSaveRequest(LocalDateTime trackingDate, GpsPoints gpsPoints) {
		this.trackingDate = trackingDate;
		this.gpsPoints = gpsPoints;
	}
}
