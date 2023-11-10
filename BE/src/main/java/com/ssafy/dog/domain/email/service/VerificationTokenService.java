package com.ssafy.dog.domain.email.service;

import com.ssafy.dog.domain.email.entity.VerificationToken;
import com.ssafy.dog.domain.email.repository.VerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class VerificationTokenService {
    private final VerificationTokenRepository verificationTokenRepository;

    private final Random random = new SecureRandom();
    private final int EXPIRATION_TIME = 30; // 분 단위

    public String createVerificationToken(String email) {
        String token = generateRandomToken();
        VerificationToken verificationToken = new VerificationToken(
                email, token, Instant.now().plus(EXPIRATION_TIME, ChronoUnit.HOURS)
        );

        verificationTokenRepository.save(verificationToken);

        return token;
    }

    private String generateRandomToken() {
        // 랜덤 토큰 생성 로직 구현
        // 0부터 2^130 - 1 범위의 random 변수로 만든 임의의 수를 32진수(숫자 0 부터 Z까지)로 변환
        return new BigInteger(130, random).toString(32);
    }

    public boolean verifyToken(String token, String email) {
        return verificationTokenRepository.findByToken(token)
                .filter(vToken -> vToken.getExpiryDate().isAfter(Instant.now())) // 만료되지 않았는지 확인
                .filter(vToken -> email.equals(vToken.getEmail())) // 이메일 주소가 일치하는지 확인
                .isPresent(); // 모든 조건을 충족하는지 확인
    }
}
