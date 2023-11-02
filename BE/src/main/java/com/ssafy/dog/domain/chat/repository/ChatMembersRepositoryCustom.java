package com.ssafy.dog.domain.chat.repository;

import java.util.List;

import com.ssafy.dog.domain.chat.entity.ChatMembers;

public interface ChatMembersRepositoryCustom {
	List<ChatMembers> getChatMembersList(Long userId);

}
