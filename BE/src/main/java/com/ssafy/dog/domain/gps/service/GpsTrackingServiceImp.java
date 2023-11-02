package com.ssafy.dog.domain.gps.service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ssafy.dog.common.error.ErrorCode;
import com.ssafy.dog.common.exception.ApiException;
import com.ssafy.dog.domain.gps.dto.GpsTrackingDeleteRequest;
import com.ssafy.dog.domain.gps.dto.GpsTrackingResponse;
import com.ssafy.dog.domain.gps.dto.GpsTrackingSaveRequest;
import com.ssafy.dog.domain.gps.entity.GpsTracking;
import com.ssafy.dog.domain.gps.entity.enums.Status;
import com.ssafy.dog.domain.gps.repository.GpsTrackingRepository;

import io.jsonwebtoken.lang.Collections;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class GpsTrackingServiceImp implements GpsTrackingService {
	private final GpsTrackingRepository gpsTrackingRepository;

	@Override
	public GpsTrackingResponse saveGpsTrackingData(GpsTrackingSaveRequest request) {
		GpsTracking savedEntity = gpsTrackingRepository.save(GpsTracking.toEntity(request));
		return GpsTrackingResponse.toResponse(savedEntity);
	}

	@Override
	public List<GpsTrackingResponse> findTrackingDataByUserLoginId(String userLoginId, String order) {
		List<GpsTracking> trackingList = gpsTrackingRepository.findAllByUserLoginIdAndStatus(userLoginId,
			Status.AVAILABLE);

		if (Collections.isEmpty(trackingList)) {
			return java.util.Collections.emptyList();
		}
		// tmp 유저 email : test@mail.com;
		if (order.equalsIgnoreCase("asc")) {
			return trackingList.stream()
				.sorted(Comparator.comparing(GpsTracking::getTrackingDate))
				.map(GpsTrackingResponse::toResponse)
				.collect(Collectors.toList());
		}

		if (order.equalsIgnoreCase("desc")) {
			return trackingList.stream()
				.sorted(Comparator.comparing(GpsTracking::getTrackingDate).reversed())
				.map(GpsTrackingResponse::toResponse)
				.collect(Collectors.toList());
		}

		throw new ApiException(ErrorCode.BAD_REQUEST, "올바른 정렬 기준을 요청해주세요. asc, desc");
	}

	@Override
	public boolean deleteTrackingRecord(GpsTrackingDeleteRequest request) {
		Optional<GpsTracking> record = gpsTrackingRepository.findById(request.getTrackingId());
		if (record.isEmpty()) {
			throw new ApiException(ErrorCode.BAD_REQUEST, "올바른 요청을 해주세요");
		}
		if (!record.get().getUserLoginId().equals("test@mail.com")) { //TODO:추후 컨텍스트 홀더 내의 아이디 가져올 예정
			throw new ApiException(ErrorCode.BAD_REQUEST, "작성자 본인만 기록을 삭제할 수 있습니다.");
		}
		record.get().changeStatus(Status.DELETED);
		gpsTrackingRepository.save(record.get());
		return true;
	}
}
