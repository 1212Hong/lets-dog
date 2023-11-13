package com.ssafy.dog.domain.fcm.config;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class FirebaseConfig {

	@PostConstruct
	public void initialize() {
		try {
			// resources에서 json 조회
			FirebaseOptions options = new FirebaseOptions.Builder()
				.setCredentials(GoogleCredentials.fromStream(
					new ClassPathResource("firebase-adminsdk.json").getInputStream())).build();
			log.info("Firebase Config 정보: {}", options.getServiceAccountId());

			if (FirebaseApp.getApps().isEmpty()) {
				FirebaseApp.initializeApp(options);
				log.info("Firebase application has been initialized");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
