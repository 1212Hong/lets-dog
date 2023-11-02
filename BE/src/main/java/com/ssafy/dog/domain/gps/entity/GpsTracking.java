package com.ssafy.dog.domain.gps.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.ssafy.dog.domain.gps.dto.GpsTrackingSaveRequest;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Document(collection = "gps_tracking")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class GpsTracking {

	@Id
	private String id;
	private String userLoginId;
	private LocalDateTime trackingDate;
	private LocalDateTime createdDate;
	private LocalDateTime modifiedDate;
	private GpsPoints gpsPoints;

	@Builder
	public GpsTracking(LocalDateTime trackingDate, GpsPoints gpsPoints) {
		this.trackingDate = trackingDate;
		this.gpsPoints = gpsPoints;
		// TODO : 토큰에서 검증해서 자동으로 삽입되도록 Listener에서 처리;
		this.userLoginId = "test@mail.com";
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public void setModifiedDate(LocalDateTime modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public static GpsTracking toEntity(GpsTrackingSaveRequest request) {
		return GpsTracking.builder()
			.gpsPoints(request.getGpsPoints())
			.trackingDate(request.getTrackingDate())
			.build();
	}

}
