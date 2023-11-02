package com.ssafy.dog.domain.gps.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ssafy.dog.domain.gps.entity.GpsTracking;

public interface GpsTrackingRepository extends MongoRepository<GpsTracking, String> {
}
