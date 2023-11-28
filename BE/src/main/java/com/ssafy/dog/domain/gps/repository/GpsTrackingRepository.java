package com.ssafy.dog.domain.gps.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.ssafy.dog.domain.gps.entity.GpsTracking;
import com.ssafy.dog.domain.gps.entity.enums.Status;

public interface GpsTrackingRepository extends MongoRepository<GpsTracking, String> {

	Page<GpsTracking> findAllByUserLoginIdAndStatus(String userLoginId, Status status, Pageable pageable);
}
