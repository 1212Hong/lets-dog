package com.ssafy.dog.domain.dog.entity;

import static javax.persistence.FetchType.*;

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
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ssafy.dog.domain.dog.model.DogDisposition;
import com.ssafy.dog.domain.dog.model.DogDispositionListConverter;
import com.ssafy.dog.domain.dog.model.DogSize;
import com.ssafy.dog.domain.user.entity.User;

import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
@Table(name = "dog")
public class Dog {
	@Id
	@Column(name = "dog_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long dogId;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "user_id")
	@JsonIgnore
	private User user;

	@NotNull
	@Size(max = 10)
	private String dogName;

	@Lob
	private String dogPicture;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Builder.Default
	private LocalDateTime dogBirthdate;

	@Size(max = 20, message = "문자 제한은 20개입니다.")
	private String dogBreed;

	@Size(min = 3, max = 5, message = "3~5개의 성향만 가질 수 있습니다.")
	@NotNull
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
		private User user;
		private String dogName;
		private String dogPicture;
		private LocalDateTime dogBirthdate;
		private String dogBreed;
		private List<DogDisposition> dogDispositionList;
		private String dogAboutMe;
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

		public DogBuilder withUser(User user) {
			this.user = user;
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
			dog.dogBreed = this.dogBreed;
			dog.dogBirthdate = this.dogBirthdate;
			dog.user = this.user;
			dog.dogAboutMe = this.dogAboutMe;
			dog.dogName = this.dogName;
			dog.dogSize = this.dogSize;
			dog.dogId = this.dogId;
			dog.dogDispositionList = this.dogDispositionList;
			return dog;
		}
	}
}

