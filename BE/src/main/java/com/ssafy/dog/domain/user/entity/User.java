package com.ssafy.dog.domain.user.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "user")
public class User implements UserDetails { // 주소 속성 아직 안 들어감
	// Entity 로서의 User 속성 및 메소드들
	@Id
	@Column(name = "user_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userId;

	@Column(unique = true)
	@Email
	@NonNull
	private String userLoginId;

	@NonNull
	private String userPw;

	@Column(unique = true)
	@NonNull
	private String userNickname;

	@ElementCollection(fetch = FetchType.EAGER)
	@Builder.Default
	private List<String> userRoles = new ArrayList<>();

	@NonNull
	@Column(name = "user_phone")
	@Size(max = 11)
	private String userPhone;

	@Lob
	private String userPicture;

	private String userAboutMe;

	private String userGender;

	@NonNull
	private Boolean userTermsAgreed;

	@NonNull
	private Boolean userIsRemoved;

	// UserDetails 로서의 User 속성 및 메소드들
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.userRoles.stream()
			.map(SimpleGrantedAuthority::new)
			.collect(Collectors.toList());
	}

	@Override
	public String getUsername() {
		return userLoginId;
	}

	@Override
	public String getPassword() {
		return "Calling Wrong methods";
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
