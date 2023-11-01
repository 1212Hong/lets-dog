package com.ssafy.dog.domain.gps.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.dog.common.api.Api;
import com.ssafy.dog.domain.gps.dto.GpsTrackingResponse;
import com.ssafy.dog.domain.gps.dto.GpsTrackingSaveRequest;
import com.ssafy.dog.domain.gps.service.GpsTrackingService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class GpsTrackingController {

	private final GpsTrackingService gpsTrackingService;

	@PostMapping("/gps")
	public Api<GpsTrackingResponse> saveGpsDate(@RequestBody GpsTrackingSaveRequest request) {
		GpsTrackingResponse response = gpsTrackingService.saveGpsTrackingData(request);
		return Api.ok(response);
	}
}
