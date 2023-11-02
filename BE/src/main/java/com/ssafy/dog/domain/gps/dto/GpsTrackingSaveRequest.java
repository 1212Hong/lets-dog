package com.ssafy.dog.domain.gps.dto;

import com.ssafy.dog.domain.gps.entity.GpsPoints;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class GpsTrackingSaveRequest {

	private GpsPoints gpsPoints;

	@Builder
	public GpsTrackingSaveRequest(GpsPoints gpsPoints) {
		this.gpsPoints = gpsPoints;
	}
}
