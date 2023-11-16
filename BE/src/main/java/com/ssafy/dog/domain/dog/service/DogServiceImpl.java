package com.ssafy.dog.domain.dog.service;

import com.ssafy.dog.common.api.Api;
import com.ssafy.dog.common.error.DogErrorCode;
import com.ssafy.dog.common.error.UserErrorCode;
import com.ssafy.dog.common.exception.ApiException;
import com.ssafy.dog.domain.dog.dto.request.DogCreateReq;
import com.ssafy.dog.domain.dog.dto.request.DogUpdateReq;
import com.ssafy.dog.domain.dog.dto.response.DogGetRes;
import com.ssafy.dog.domain.dog.entity.Dog;
import com.ssafy.dog.domain.dog.mapper.DogMapper;
import com.ssafy.dog.domain.dog.model.DogDisposition;
import com.ssafy.dog.domain.dog.model.DogSize;
import com.ssafy.dog.domain.dog.repository.DogRepository;
import com.ssafy.dog.domain.user.entity.User;
import com.ssafy.dog.domain.user.repository.UserRepository;
import com.ssafy.dog.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DogServiceImpl implements DogService {
    private final DogRepository dogRepository;
    private final UserRepository userRepository;
    private final DogMapper dogMapper;

    @Override
    public Dog create(DogCreateReq dogCreateReq, Errors errors) {

        if (dogCreateReq.getDogName().length() > 10) {
            throw new ApiException(DogErrorCode.NAME_SIZE_ERROR);
        }

        if (dogCreateReq.getDogBreed().length() > 20) {
            throw new ApiException(DogErrorCode.BREED_SIZE_ERROR);
        }

        if (dogCreateReq.getDogDispositionList().size() < 3 || dogCreateReq.getDogDispositionList().size() > 5) {
            throw new ApiException(DogErrorCode.DISPOSITION_LIST_SIZE_ERROR);
        }

        if (!dogCreateReq.getDogDispositionList().stream()
                .allMatch(this::isValidDisposition)) {
            throw new ApiException(DogErrorCode.DISPOSITION_VALUE_ERROR);
        }

        List<DogDisposition> dispositions = dogCreateReq.getDogDispositionList();
        Set<DogDisposition> uniqueDispositions = new HashSet<>(dispositions);
        if (dispositions.size() != uniqueDispositions.size()) {
            throw new ApiException(DogErrorCode.DISPOSITION_DUPLICATE_ERROR);
        }

        if (dogCreateReq.getDogAboutMe().length() > 200) {
            throw new ApiException(DogErrorCode.ABOUTME_SIZE_ERROR);
        }

        if (!isValidDogSize(dogCreateReq.getDogSize())) {
            throw new ApiException(DogErrorCode.SIZE_VALUE_ERROR);
        }

        if (errors.hasErrors()) {
            // 유효성 통과 못한 필드와 메시지를 핸들링
            Map<String, String> validatorResult = validateHandling(errors);

            // 각 유효성 실패에 대한 로그 출력
            validatorResult.forEach((field, message) -> log.error("INVALID_DOG_UPDATE_FIELD: {}{, {}", field, message));

            // 유효성 검사 실패 응답 리턴
            throw new ApiException(DogErrorCode.INVALID_DOG_UPDATE_FIELDS);
        }

        Dog dog = dogMapper.toEntity(dogCreateReq);
        return dogRepository.save(dog);
    }

    @Override
    @Transactional
    public Api<List<DogGetRes>> getDogs(Long userId) {
        Optional<User> user = userRepository.findByUserId(userId);
        if (user.isEmpty()) {
            throw new ApiException(UserErrorCode.USER_NOT_FOUND);
        }

        List<Dog> dogs = dogRepository.findAllByUser(user.get());

        List<DogGetRes> dogGetResList = dogs.stream()
                .map(DogGetRes::toDto)
                .collect(Collectors.toList());

        return Api.ok(dogGetResList);
    }

    @Override
    @Transactional
    public Api<DogGetRes> getDogByDogId(Long dogId) {
        Optional<Dog> dog = dogRepository.findById(dogId);
        if (dog.isEmpty()) {
            throw new ApiException(DogErrorCode.DOG_NOT_FOUND);
        }

        DogGetRes dogGetRes = DogGetRes.toDto(dog.get());
        return Api.ok(dogGetRes);
    }

    @Override
    @Transactional
    public Dog update(DogUpdateReq dogUpdateReq, Errors errors) {
        Dog dog = dogRepository.findById(dogUpdateReq.getDogId())
                .orElseThrow(() -> new ApiException(DogErrorCode.DOG_NOT_FOUND));

        Long userId = SecurityUtils.getUserId();

        // 개가 로그인 한 유저 소유인지 확인
        if (!dog.getUser().getUserId().equals(userId)) {
            throw new ApiException(DogErrorCode.NOT_DOG_OWNER);
        }

        if (dogUpdateReq.getDogName().length() > 10) {
            throw new ApiException(DogErrorCode.NAME_SIZE_ERROR);
        }

        if (dogUpdateReq.getDogBreed().length() > 20) {
            throw new ApiException(DogErrorCode.BREED_SIZE_ERROR);
        }

        if (dogUpdateReq.getDogDispositionList().size() < 3 || dogUpdateReq.getDogDispositionList().size() > 5) {
            throw new ApiException(DogErrorCode.DISPOSITION_LIST_SIZE_ERROR);
        }

        if (!dogUpdateReq.getDogDispositionList().stream()
                .allMatch(this::isValidDisposition)) {
            throw new ApiException(DogErrorCode.DISPOSITION_VALUE_ERROR);
        }

        List<DogDisposition> dispositions = dogUpdateReq.getDogDispositionList();
        Set<DogDisposition> uniqueDispositions = new HashSet<>(dispositions);
        if (dispositions.size() != uniqueDispositions.size()) {
            throw new ApiException(DogErrorCode.DISPOSITION_DUPLICATE_ERROR);
        }

        if (dogUpdateReq.getDogAboutMe().length() > 200) {
            throw new ApiException(DogErrorCode.ABOUTME_SIZE_ERROR);
        }

        if (!isValidDogSize(dogUpdateReq.getDogSize())) {
            throw new ApiException(DogErrorCode.SIZE_VALUE_ERROR);
        }

        if (errors.hasErrors()) {
            // 유효성 통과 못한 필드와 메시지를 핸들링
            Map<String, String> validatorResult = validateHandling(errors);

            // 각 유효성 실패에 대한 로그 출력
            validatorResult.forEach((field, message) -> log.error("INVALID_DOG_UPDATE_FIELD: {}{, {}", field, message));

            // 유효성 검사 실패 응답 리턴
            throw new ApiException(DogErrorCode.INVALID_DOG_UPDATE_FIELDS);
        }

        // Update dog fields
        dog.updateName(dogUpdateReq.getDogName());
        dog.updatePicture(dogUpdateReq.getDogPicture());
        dog.updateBirthdate(dogUpdateReq.getDogBirthdate().atStartOfDay());
        dog.updateBreed(dogUpdateReq.getDogBreed());
        dog.updateDispositioinList(dogUpdateReq.getDogDispositionList());
        dog.updateAboutMe(dogUpdateReq.getDogAboutMe());
        dog.updateSize(dogUpdateReq.getDogSize());

        return dogRepository.save(dog);
    }

    @Transactional
    public void deleteDog(Long dogId, Long userId) {
        Dog dog = dogRepository.findById(dogId)
                .orElseThrow(() -> new ApiException(DogErrorCode.DOG_NOT_FOUND));

        if (!dog.getUser().getUserId().equals(userId)) {
            throw new ApiException(DogErrorCode.NOT_DOG_OWNER);
        }

        dogRepository.delete(dog);
    }

    // 유효성 체크
    @Transactional(readOnly = true)
    public Map<String, String> validateHandling(Errors errors) {
        Map<String, String> validatorResult = new HashMap<>();

        // 유효성 검사에 실패한 필드 목록을 받음
        for (FieldError error : errors.getFieldErrors()) {
            String validKeyName = String.format("%s", error.getField());
            validatorResult.put(validKeyName, error.getDefaultMessage());
        }

        return validatorResult;
    }

    // dogDispositionList 의 원소가 모두 DogDispositon ENUM에 있는 값들인지 체크
    private boolean isValidDisposition(DogDisposition disposition) {
        for (DogDisposition d : DogDisposition.values()) {
            if (d.equals(disposition)) {
                return true;
            }
        }
        return false;
    }

    // 받은 size 가 "SMALL", "MEDIUM", "LARGE" 중 하나인지 체크
    private boolean isValidDogSize(DogSize size) {
        for (DogSize ds : DogSize.values()) {
            if (ds.equals(size)) {
                return true;
            }
        }
        return false;
    }
}
