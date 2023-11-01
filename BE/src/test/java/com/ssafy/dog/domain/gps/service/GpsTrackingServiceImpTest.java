package com.ssafy.dog.domain.gps.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.ssafy.dog.common.exception.ApiException;
import com.ssafy.dog.domain.gps.dto.GpsTrackingResponse;
import com.ssafy.dog.domain.gps.dto.GpsTrackingSaveRequest;
import com.ssafy.dog.domain.gps.entity.GpsPoints;
import com.ssafy.dog.domain.gps.entity.GpsTracking;
import com.ssafy.dog.domain.gps.repository.GpsTrackingRepository;

import lombok.extern.slf4j.Slf4j;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Slf4j
class GpsTrackingServiceImpTest {
	@InjectMocks
	private GpsTrackingServiceImp gpsTrackingServiceImp;

	@Mock
	private GpsTrackingRepository gpsTrackingRepository;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void gps데이터_request를_성공적으로_저장() {
		List<Double> point1 = Arrays.asList(40.712, 74.23);
		List<Double> point2 = Arrays.asList(41.712, 73.23);
		List<List<Double>> gpsList = Arrays.asList(point1, point2);
		GpsPoints gpsPoints = new GpsPoints(gpsList);
		GpsTrackingSaveRequest request = GpsTrackingSaveRequest.builder()
			.gpsPoints(gpsPoints)
			.trackingDate(LocalDateTime.now().minusDays(1))
			.build();
		GpsTracking gpsTracking = GpsTracking.toEntity(request);
		GpsTrackingResponse expectedResponse = GpsTrackingResponse.toResponse(gpsTracking);

		when(gpsTrackingRepository.save(any(GpsTracking.class))).thenReturn(gpsTracking);

		GpsTrackingResponse actualResponse = gpsTrackingServiceImp.saveGpsTrackingData(request);

		log.info("expected = {} : actual = {}", expectedResponse.getGpsPoints(), actualResponse.getGpsPoints());
		assertEquals(expectedResponse.getGpsPoints(), actualResponse.getGpsPoints());
		assertEquals(expectedResponse.getTrackingDate(), actualResponse.getTrackingDate());
	}

	@Test
	void null_입력시_ApiException_badReqeust_발생() {
		assertThrows(ApiException.class, () -> {
			gpsTrackingServiceImp.saveGpsTrackingData(null);
		});
	}

}