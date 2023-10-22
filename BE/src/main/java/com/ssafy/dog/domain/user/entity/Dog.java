package com.ssafy.dog.domain.user.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "Dog")
public class Dog {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long dogId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "userId", referencedColumnName = "userId")
  private User user;

  private String dogName;
  private String dogPicture;
  private LocalDateTime dogBirthdate;
  private String dogBreed;
  private String dogDisposition;
  private String dogAboutMe;
  private String dogSize;
}