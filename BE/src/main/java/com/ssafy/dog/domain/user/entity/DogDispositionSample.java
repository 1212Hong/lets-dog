package com.ssafy.dog.domain.user.entity;


import lombok.Data;
import javax.persistence.*;

@Data
@Entity
@Table(name = "DogDispositionSample")
public class DogDispositionSample {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String sampleId;
  private String sampleDisposition;
}
