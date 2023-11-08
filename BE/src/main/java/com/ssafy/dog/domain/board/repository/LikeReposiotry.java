package com.ssafy.dog.domain.board.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ssafy.dog.domain.board.entity.LikeEntity;

@Repository
public interface LikeReposiotry extends JpaRepository<LikeEntity, Long> {
	List<LikeEntity> findByUserUserNickname(String userNickname);
}
