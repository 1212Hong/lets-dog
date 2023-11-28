package com.ssafy.dog.domain.gps.listener;

import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.data.mongodb.core.mapping.event.BeforeSaveEvent;
import org.springframework.stereotype.Component;

import com.ssafy.dog.domain.gps.entity.GpsTracking;

@Component
public class GpsTrackingModelListener extends AbstractMongoEventListener<GpsTracking> {

	@Override
	public void onBeforeConvert(BeforeConvertEvent<GpsTracking> event) {
		GpsTracking gpsTracking = event.getSource();
		if (gpsTracking.getCreatedDate() == null) {
			gpsTracking.setCreatedDate(LocalDateTime.now(ZoneId.of("Asia/Seoul")));
		}
	}

	@Override
	public void onBeforeSave(BeforeSaveEvent<GpsTracking> event) {
		GpsTracking gpsTracking = event.getSource();
		gpsTracking.setModifiedDate(LocalDateTime.now(ZoneId.of("Asia/Seoul")));
	}
}
