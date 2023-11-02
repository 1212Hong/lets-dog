package com.ssafy.dog.domain.gps.repository;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;

import com.ssafy.dog.domain.gps.entity.GpsPoints;
import com.ssafy.dog.domain.gps.entity.GpsTracking;
import com.ssafy.dog.domain.gps.listener.GpsTrackingModelListener;

@DataMongoTest // test 실행시 application enable JPA auditing 주석 필요
@Import(GpsTrackingModelListener.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class GpsTrackingRepositoryTest {

	@Autowired
	private GpsTrackingRepository gpsTrackingRepository;

	@BeforeEach
	void setup() {
		gpsTrackingRepository.deleteAll();
	}

	@AfterEach
	void tearDown() {
		gpsTrackingRepository.deleteAll();
	}

	@Test
	void gps추적_기록을_저장() {
		//given
		List<Double> point1 = Arrays.asList(40.712, 74.23);
		List<Double> point2 = Arrays.asList(41.712, 73.23);
		List<List<Double>> gpsList = Arrays.asList(point1, point2);
		GpsPoints gpsPoints = new GpsPoints(gpsList);
		GpsTracking gpsTracking = new GpsTracking(LocalDateTime.now(), gpsPoints);
		//when
		GpsTracking savedEntity = gpsTrackingRepository.save(gpsTracking);
		Optional<GpsTracking> getEntity = gpsTrackingRepository.findById(savedEntity.getId());
		//then
		System.out.println(savedEntity.getCreatedDate());
		assertThat(getEntity).isNotEmpty();
		assertThat(getEntity.get().getGpsPoints()).isEqualTo(gpsPoints);
	}
}