package com.ssafy.dog.domain.dog.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import com.ssafy.dog.common.utils.StringListConverter;
import com.ssafy.dog.domain.dog.model.DogSize;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Entity
@Getter
@Builder
@Table(name = "dog")
public class Dog {
	@Id
	@Column(name = "dog_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long dogId;

	@NonNull
	@Size(max = 10)
	private String dogName;

	@Lob
	private String dogPicture;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Builder.Default
	private LocalDateTime dogBirthdate;

	@Size(max = 20, message = "문자 제한은 20개입니다.")
	private String dogBreed;

	@Convert(converter = StringListConverter.class)
	@Size(max = 5, message = "성향은 5개까지 등록 가능합니다.")
	private List<String> dogDisposition = new ArrayList<>();

	@Size(max = 200)
	private String dogAboutMe;

	@Enumerated(EnumType.STRING)
	private DogSize dogSize;

}
