package com.ssafy.dog.domain.dog.dto.response;

import com.ssafy.dog.domain.dog.entity.Dog;
import com.ssafy.dog.domain.dog.model.DogDisposition;
import com.ssafy.dog.domain.dog.model.DogSize;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class DogGetRes {
    private Long dogId;
    private String dogAboutMe;
    private LocalDateTime dogBirthdate;
    private String dogBreed;
    private List<DogDisposition> dogDispositionList;
    private String dogName;
    private String dogPicture;
    private DogSize dogSize;

    public static DogGetRes toDto(Dog entity) {
        return DogGetRes.builder()
                .dogId(entity.getDogId())
                .dogAboutMe(entity.getDogAboutMe())
                .dogBirthdate(entity.getDogBirthdate())
                .dogBreed(entity.getDogBreed())
                .dogDispositionList(entity.getDogDispositionList())
                .dogName(entity.getDogName())
                .dogPicture(entity.getDogPicture())
                .dogSize(entity.getDogSize())
                .build();
    }
}
