package com.ssafy.dog.domain.dog.dto.request;

import com.ssafy.dog.domain.dog.model.DogDisposition;
import com.ssafy.dog.domain.dog.model.DogDispositionListConverter;
import com.ssafy.dog.domain.dog.model.DogSize;
import lombok.Getter;
import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.Convert;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
public class DogCreateReq {
    @NotNull
    @Size(max = 10)
    private String dogName;

    @Lob
    private String dogPicture;

    private String dogBirthdate;

    @Size(max = 20, message = "문자 제한은 20개입니다.")
    private String dogBreed;

    @Size(min = 3, max = 5, message = "3~5개의 성향만 가질 수 있습니다.")
    @UniqueElements
    @Convert(converter = DogDispositionListConverter.class)
    private List<DogDisposition> dogDispositionList;
    /*
     * DogDisposition 의 생김새 : CONSTANT("key", "description")
     * DogDisposition 의 길이 : 20
     * 아래는 DogDisposition 의 일부
     * public enum DogDisposition { FRIENDLY("친화적", "다른 강아지나 사람들에게 친화적으로..."), ... }
     * */

    @Size(max = 200)
    private String dogAboutMe;

    @Enumerated(EnumType.STRING)
    private DogSize dogSize;
}
