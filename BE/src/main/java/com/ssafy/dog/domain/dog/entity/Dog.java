package com.ssafy.dog.domain.dog.entity;

import java.time.LocalDateTime;
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

import com.ssafy.dog.domain.dog.model.DogDisposition;
import com.ssafy.dog.domain.dog.model.DogDispositionListConverter;
import com.ssafy.dog.domain.dog.model.DogSize;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Entity
@Getter
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

	@Size(max = 5, message = "최대 5개의 성향만 가질 수 있습니다.")
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

	public static final class DogBuilder {
		private Long dogId;
		private @NonNull @Size(max = 10) String dogName;
		private String dogPicture;
		private LocalDateTime dogBirthdate;
		private @Size(max = 20, message = "문자 제한은 20개입니다.") String dogBreed;
		private @Size(max = 5, message = "최대 5개의 성향만 가질 수 있습니다.") List<DogDisposition> dogDispositionList;
		private @Size(max = 200) String dogAboutMe;
		private DogSize dogSize;

		private DogBuilder() {
		}

		public static DogBuilder aDog() {
			return new DogBuilder();
		}

		public DogBuilder withDogId(Long dogId) {
			this.dogId = dogId;
			return this;
		}

		public DogBuilder withDogName(String dogName) {
			this.dogName = dogName;
			return this;
		}

		public DogBuilder withDogPicture(String dogPicture) {
			this.dogPicture = dogPicture;
			return this;
		}

		public DogBuilder withDogBirthdate(LocalDateTime dogBirthdate) {
			this.dogBirthdate = dogBirthdate;
			return this;
		}

		public DogBuilder withDogBreed(String dogBreed) {
			this.dogBreed = dogBreed;
			return this;
		}

		public DogBuilder withDogDispositionList(List<DogDisposition> dogDispositionList) {
			this.dogDispositionList = dogDispositionList;
			return this;
		}

		public DogBuilder withDogAboutMe(String dogAboutMe) {
			this.dogAboutMe = dogAboutMe;
			return this;
		}

		public DogBuilder withDogSize(DogSize dogSize) {
			this.dogSize = dogSize;
			return this;
		}

		public Dog build() {
			Dog dog = new Dog();
			dog.dogPicture = this.dogPicture;
			dog.dogAboutMe = this.dogAboutMe;
			dog.dogId = this.dogId;
			dog.dogSize = this.dogSize;
			dog.dogBreed = this.dogBreed;
			dog.dogName = this.dogName;
			dog.dogDispositionList = this.dogDispositionList;
			dog.dogBirthdate = this.dogBirthdate;
			return dog;
		}
	}
}
