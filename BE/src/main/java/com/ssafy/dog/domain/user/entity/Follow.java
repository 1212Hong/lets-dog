package com.ssafy.dog.domain.user.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Entity
@Getter
@RequiredArgsConstructor
@Table(name = "Follow")
public class Follow {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long followId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "userId", referencedColumnName = "userId")
  private User user;

  private Long followTargetId;
  private LocalDateTime followCreatedAt;
}