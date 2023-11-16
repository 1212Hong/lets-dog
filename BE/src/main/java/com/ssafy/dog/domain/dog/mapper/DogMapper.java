package com.ssafy.dog.domain.dog.mapper;

import com.ssafy.dog.common.error.DogErrorCode;
import com.ssafy.dog.common.error.UserErrorCode;
import com.ssafy.dog.common.exception.ApiException;
import com.ssafy.dog.domain.dog.dto.request.DogCreateReq;
import com.ssafy.dog.domain.dog.entity.Dog;
import com.ssafy.dog.domain.user.repository.UserRepository;
import com.ssafy.dog.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Component
@Slf4j
@RequiredArgsConstructor
public class DogMapper {
    private final UserRepository userRepository; // 언제 static 이 붙고 언제 final 이 붙어야 하는가

    public Dog toEntity(DogCreateReq dogCreateReq) {
        // 기본 이미지 URL
        String defaultImageUrl = "http://k9c205.p.ssafy.io:9000/api/file/7e6c6a6b-44c2-405e-869e-22bce7b67cee.png_encoded.jpg";

        // dogPicture 값이 비어있는지 확인하고, 비어있으면 기본 URL을 사용
        String dogPicture = dogCreateReq.getDogPicture().isEmpty() ? defaultImageUrl : dogCreateReq.getDogPicture();

        LocalDateTime birthdate;
        try {
            LocalDate date = LocalDate.parse(dogCreateReq.getDogBirthdate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            birthdate = date.atStartOfDay();
        } catch (DateTimeParseException e) {
            throw new ApiException(DogErrorCode.INVALID_BIRTHDATE_FORMAT);
        }


        return Dog.DogBuilder.aDog()
                .withUser(userRepository.findByUserId(SecurityUtils.getUserId())
                        .orElseThrow(() -> new ApiException(UserErrorCode.USER_NOT_FOUND)))
                .withDogName(dogCreateReq.getDogName())
                .withDogPicture(dogPicture)
                .withDogBirthdate(birthdate)
                .withDogBreed(dogCreateReq.getDogBreed())
                .withDogDispositionList(dogCreateReq.getDogDispositionList())
                .withDogAboutMe(dogCreateReq.getDogAboutMe())
                .withDogSize(dogCreateReq.getDogSize())
                .build();
    }
}
