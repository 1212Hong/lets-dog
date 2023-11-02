package com.ssafy.dog.domain.gps.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ssafy.dog.domain.gps.entity.GpsTracking;
import com.ssafy.dog.domain.gps.entity.enums.Status;

public interface GpsTrackingRepository extends MongoRepository<GpsTracking, String> {

	List<GpsTracking> findAllByUserLoginIdAndStatus(String userLoginId, Status status);
}
