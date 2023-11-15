package com.ssafy.dog.domain.dog.dto.request;

import com.ssafy.dog.domain.dog.model.DogDisposition;
import com.ssafy.dog.domain.dog.model.DogDispositionListConverter;
import com.ssafy.dog.domain.dog.model.DogSize;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Convert;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Getter
public class DogUpdateReq {
    private Long dogId;

    @Size(max = 10)
    private String dogName;

    @Lob
    private String dogPicture;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime dogBirthdate;

    private String dogBreed;

    @Size(min = 3, max = 5, message = "3~5개의 성향만 가질 수 있습니다.")
    @Convert(converter = DogDispositionListConverter.class)
    private List<DogDisposition> dogDispositionList;

    @Size(max = 200)
    private String dogAboutMe;

    @Enumerated(EnumType.STRING)
    private DogSize dogSize;

}
