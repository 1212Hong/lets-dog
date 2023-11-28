package com.ssafy.dog.domain.gps.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GpsTrackingDeleteRequest {
	private String trackingId;
}
