package com.ssafy.dog.domain.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.dog.domain.board.entity.FileUrl;

public interface FileUrlRepository extends JpaRepository<FileUrl, Long> {

}
