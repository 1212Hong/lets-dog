package com.ssafy.dog.domain.gps.service;

import com.ssafy.dog.domain.gps.dto.GpsTrackingResponse;
import com.ssafy.dog.domain.gps.dto.GpsTrackingSaveRequest;

public interface GpsTrackingService {

	GpsTrackingResponse saveGpsTrackingData(GpsTrackingSaveRequest request);

	void findTrackingDataByUserLoginId();

}
