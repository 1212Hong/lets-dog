package com.ssafy.dog.domain.user.entity;


import javax.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;


@Entity
@Getter
@NoArgsConstructor
@Table(name = "DogDispositionSample")
public class DogDispositionSample {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String sampleId;
  private String sampleDisposition;
}
