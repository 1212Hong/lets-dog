package com.ssafy.dog.domain.gps.service;

import com.ssafy.dog.domain.gps.dto.GpsTrackingDataResponse;
import com.ssafy.dog.domain.gps.dto.GpsTrackingDeleteRequest;
import com.ssafy.dog.domain.gps.dto.GpsTrackingResponse;
import com.ssafy.dog.domain.gps.dto.GpsTrackingSaveRequest;

public interface GpsTrackingService {

	GpsTrackingResponse saveGpsTrackingData(GpsTrackingSaveRequest request);

	GpsTrackingDataResponse findTrackingDataByUserLoginId(String userLoginId, int page, int size, String order);

	boolean deleteTrackingRecord(GpsTrackingDeleteRequest request);

}
