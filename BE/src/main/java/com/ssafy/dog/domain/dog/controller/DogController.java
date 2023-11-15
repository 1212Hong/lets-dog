package com.ssafy.dog.domain.dog.controller;

import com.ssafy.dog.common.api.Api;
import com.ssafy.dog.common.error.UserErrorCode;
import com.ssafy.dog.common.exception.ApiException;
import com.ssafy.dog.domain.dog.dto.request.DogCreateReq;
import com.ssafy.dog.domain.dog.dto.request.DogUpdateReq;
import com.ssafy.dog.domain.dog.dto.response.DogGetRes;
import com.ssafy.dog.domain.dog.entity.Dog;
import com.ssafy.dog.domain.dog.repository.DogRepository;
import com.ssafy.dog.domain.dog.service.DogService;
import com.ssafy.dog.domain.user.entity.User;
import com.ssafy.dog.domain.user.repository.UserRepository;
import com.ssafy.dog.util.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequestMapping("/api/dog")
@RequiredArgsConstructor
@RestController
public class DogController {

    private final UserRepository userRepository;
    private final DogRepository dogRepository;
    private final DogService dogService;

    @PostMapping
    @Operation(summary = "개 생성")
    public Api<Map<String, Long>> createNewDog(@Valid @RequestBody DogCreateReq dogCreateReq) {
        Dog newDog = dogService.create(dogCreateReq);
        Map<String, Long> ret = new HashMap<String, Long>();
        ret.put("dog_id", newDog.getDogId());
        return Api.ok(ret);
    }

    @GetMapping("/mine")
    @Operation(summary = "나의 개 정보 불러오기")
    public Api<List<DogGetRes>> readMyDogs() {
        return dogService.getDogs(SecurityUtils.getUserId());
    }

    @GetMapping("/bynick/{nickname}")
    @Operation(summary = "닉네임으로 개 정보 불러오기")
    public Api<List<DogGetRes>> readDogs(@PathVariable String nickname) {
        Optional<User> user = userRepository.findByUserNickname(nickname);
        if (user.isEmpty()) {
            throw new ApiException(UserErrorCode.USER_NOT_FOUND);
        }
        return dogService.getDogs(user.get().getUserId());
    }

    @GetMapping("/bypk/{dogId}")
    @Operation(summary = "dogId(PK)로 개 정보 불러오기")
    public Api<DogGetRes> readDogByDogId(@PathVariable Long dogId) {
        return dogService.getDogByDogId(dogId);
    }

    @PutMapping
    @Operation(summary = "개 정보 업데이트")
    public Api<Map<String, Long>> updateDog(@Valid @RequestBody DogUpdateReq dogUpdateReq, Errors errors) {
        Dog newDog = dogService.update(dogUpdateReq, errors);
        Map<String, Long> ret = new HashMap<>();
        ret.put("dogId", newDog.getDogId());
        return Api.ok(ret);
    }

    @DeleteMapping("/{dogId}")
    @Operation(summary = "개 정보 삭제")
    public Api<?> deleteDog(@Valid @PathVariable("dogId") Long dogId) {
        dogService.deleteDog(dogId, SecurityUtils.getUserId());

        Map<String, Long> ret = new HashMap<>();
        ret.put("dogId", dogId);
        return Api.ok(ret);
    }
}