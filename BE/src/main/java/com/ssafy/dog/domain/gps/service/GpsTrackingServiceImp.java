package com.ssafy.dog.domain.gps.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.dog.domain.gps.dto.GpsTrackingResponse;
import com.ssafy.dog.domain.gps.dto.GpsTrackingSaveRequest;
import com.ssafy.dog.domain.gps.entity.GpsTracking;
import com.ssafy.dog.domain.gps.repository.GpsTrackingRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class GpsTrackingServiceImp implements GpsTrackingService {
	private final GpsTrackingRepository gpsTrackingRepository;

	@Override
	@Transactional
	public GpsTrackingResponse saveGpsTrackingData(GpsTrackingSaveRequest request) {
		GpsTracking savedEntity = gpsTrackingRepository.save(GpsTracking.toEntity(request));
		return GpsTrackingResponse.toResponse(savedEntity);
	}

	public void findTrackingDataByUserLoginId() {

	}
}
