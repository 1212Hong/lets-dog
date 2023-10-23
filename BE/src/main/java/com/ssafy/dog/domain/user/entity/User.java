package com.ssafy.dog.domain.user.entity;

import com.ssafy.dog.domain.board.entity.Board;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Entity
@Getter
@RequiredArgsConstructor
@Table(name = "User")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long userId;
  private String userLoginId;
  private String userPw;
  private String userNickname;
  private LocalDateTime userCreatedAt;
  private LocalDateTime userUpdatedAt;
  private String userAboutMe;
  private String userGender;
  private Boolean userTermsAgreed;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<Follow> follows;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<Dog> dogs;

  @OneToMany(mappedBy = "user")
  private List<Board> boards;
}