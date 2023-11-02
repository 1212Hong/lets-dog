package com.ssafy.dog.domain.gps.service;

import java.util.List;

import com.ssafy.dog.domain.gps.dto.GpsTrackingDeleteRequest;
import com.ssafy.dog.domain.gps.dto.GpsTrackingResponse;
import com.ssafy.dog.domain.gps.dto.GpsTrackingSaveRequest;

public interface GpsTrackingService {

	GpsTrackingResponse saveGpsTrackingData(GpsTrackingSaveRequest request);

	List<GpsTrackingResponse> findTrackingDataByUserLoginId(String userLoginId, String order);

	boolean deleteTrackingRecord(GpsTrackingDeleteRequest request);

}
