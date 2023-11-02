package com.ssafy.dog.domain.gps.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ssafy.dog.domain.gps.entity.GpsTracking;
import com.ssafy.dog.domain.gps.entity.enums.Status;

public interface GpsTrackingRepository extends MongoRepository<GpsTracking, String> {

	List<GpsTracking> findAllByUserLoginIdAndStatus(String userLoginId, Status status);

	Optional<GpsTracking> findFirstByUserLoginIdAndTrackingDate(String userLoginId, LocalDateTime trackingDate);
}
