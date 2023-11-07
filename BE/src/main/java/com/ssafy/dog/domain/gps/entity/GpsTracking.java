package com.ssafy.dog.domain.gps.entity;

import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.ssafy.dog.domain.gps.dto.GpsTrackingSaveRequest;
import com.ssafy.dog.domain.gps.entity.enums.Status;
import com.ssafy.dog.util.SecurityUtils;

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
	private Status status;

	@Builder
	public GpsTracking(LocalDateTime trackingDate, GpsPoints gpsPoints, Status status) {
		this.trackingDate = trackingDate;
		this.gpsPoints = gpsPoints;
		this.status = status;
		this.userLoginId = SecurityUtils.getUserLoginId();
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public void setModifiedDate(LocalDateTime modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public void changeStatus(Status status) {
		this.status = status;
	}

	public static GpsTracking toEntity(GpsTrackingSaveRequest request) {
		return GpsTracking.builder()
			.trackingDate(LocalDateTime.now(ZoneId.of("Asia/Seoul")))
			.gpsPoints(request.getGpsPoints())
			.status(Status.AVAILABLE)
			.build();
	}

}
