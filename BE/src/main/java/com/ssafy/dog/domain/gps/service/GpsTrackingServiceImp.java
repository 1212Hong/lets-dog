package com.ssafy.dog.domain.gps.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.ssafy.dog.common.error.ErrorCode;
import com.ssafy.dog.common.exception.ApiException;
import com.ssafy.dog.domain.gps.dto.GpsTrackingDeleteRequest;
import com.ssafy.dog.domain.gps.dto.GpsTrackingResponse;
import com.ssafy.dog.domain.gps.dto.GpsTrackingSaveRequest;
import com.ssafy.dog.domain.gps.entity.GpsTracking;
import com.ssafy.dog.domain.gps.entity.enums.Status;
import com.ssafy.dog.domain.gps.repository.GpsTrackingRepository;
import com.ssafy.dog.util.SecurityUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class GpsTrackingServiceImp implements GpsTrackingService {

	private static final int PAGE = 0;
	private static final int SIZE = 5;

	private final GpsTrackingRepository gpsTrackingRepository;

	@Override
	public GpsTrackingResponse saveGpsTrackingData(GpsTrackingSaveRequest request) {
		GpsTracking savedEntity = gpsTrackingRepository.save(GpsTracking.toEntity(request));
		return GpsTrackingResponse.toResponse(savedEntity);
	}

	@Override
	public List<GpsTrackingResponse> findTrackingDataByUserLoginId(String userLoginId, String order) {
		Sort sort = Sort.by("trackingDate");
		sort = order.equalsIgnoreCase("asc") ? sort.ascending() : sort.descending();
		Pageable pageable = PageRequest.of(PAGE, SIZE, sort);

		if (userLoginId == null) {
			userLoginId = SecurityUtils.getUserLoginId();
		}

		Page<GpsTracking> trackingList = gpsTrackingRepository.findAllByUserLoginIdAndStatus(
			userLoginId, Status.AVAILABLE, pageable);

		if (trackingList.isEmpty()) {
			return java.util.Collections.emptyList();
		}

		return trackingList.getContent().stream()
			.map(GpsTrackingResponse::toResponse)
			.collect(Collectors.toList());

	}

	@Override
	public boolean deleteTrackingRecord(GpsTrackingDeleteRequest request) {
		Optional<GpsTracking> record = gpsTrackingRepository.findById(request.getTrackingId());
		if (record.isEmpty()) {
			throw new ApiException(ErrorCode.BAD_REQUEST, "올바른 요청을 해주세요");
		}
		if (!record.get().getUserLoginId().equals(SecurityUtils.getUserLoginId())) {
			throw new ApiException(ErrorCode.BAD_REQUEST, "작성자 본인만 기록을 삭제할 수 있습니다.");
		}
		record.get().changeStatus(Status.DELETED);
		gpsTrackingRepository.save(record.get());
		return true;
	}
}
