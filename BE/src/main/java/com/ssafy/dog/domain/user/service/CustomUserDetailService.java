package com.ssafy.dog.domain.user.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ssafy.dog.domain.user.Model.UserRole;
import com.ssafy.dog.domain.user.entity.User;
import com.ssafy.dog.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {
	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String userLoginId) throws UsernameNotFoundException {
		Optional<User> user = this.userRepository.findByUserLoginId(userLoginId);
		if (user.isEmpty()) {
			throw new UsernameNotFoundException("사용자를 찾을 수 없습니다."); // 일단 구현 하고 Api Exception 작성하기
		}
		List<GrantedAuthority> authorities = new ArrayList<>();
		if ("kihong2424@gmail.com".equals(userLoginId)) {
			authorities.add(new SimpleGrantedAuthority(UserRole.ADMIN.getValue()));
		} else {
			authorities.add(new SimpleGrantedAuthority(UserRole.USER.getValue()));
		}
		return new org.springframework.security.core.userdetails.User(
			user.get().getUserLoginId(),
			user.get().getUserPw(),
			authorities
		);
	}
}
