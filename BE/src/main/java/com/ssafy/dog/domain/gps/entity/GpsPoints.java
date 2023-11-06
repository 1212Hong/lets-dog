package com.ssafy.dog.domain.gps.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ssafy.dog.common.error.GpsErrorCode;
import com.ssafy.dog.common.exception.ApiException;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
public class GpsPoints {

	private List<List<Double>> gpsPoints;

	@JsonCreator
	public GpsPoints(@JsonProperty("gps_points") List<List<Double>> gpsPoints) {
		validate(gpsPoints);
		this.gpsPoints = gpsPoints;
	}

	private void validate(List<List<Double>> gpsPoints) {
		if (gpsPoints == null || gpsPoints.isEmpty()) {
			throw new ApiException(GpsErrorCode.GPS_POINT_NOT_VALID, "GPS값을 입력해주세요");
		}

		for (List<Double> gpsPoint : gpsPoints) {
			log.info("{}", gpsPoint);
			if (gpsPoint.size() != 2) {
				throw new ApiException(GpsErrorCode.GPS_POINT_NOT_VALID, "GPS값은 위도와 경도로 이루어저야 합니다.");
			}
		}
	}
}
