package com.ssafy.dog.domain.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.dog.domain.chat.entity.ChatMembers;

public interface ChatMembersRepository extends JpaRepository<ChatMembers, Long>, ChatMembersRepositoryCustom {
}


