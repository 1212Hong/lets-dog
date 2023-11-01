package com.ssafy.dog.domain.user.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.ssafy.dog.common.auditing.BaseTimeEntity;
import com.ssafy.dog.domain.board.entity.Board;
import com.ssafy.dog.domain.board.entity.Comment;
import com.ssafy.dog.domain.dog.entity.Dog;
import com.ssafy.dog.domain.user.model.UserGender;
import com.ssafy.dog.domain.user.model.UserRole;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user")
public class User extends BaseTimeEntity implements UserDetails { // 주소 속성 아직 안 들어감
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

	@Column(name = "user_role")
	@Enumerated(EnumType.STRING)
	private UserRole userRole;

	@NonNull
	@Column(name = "user_phone")
	@Size(max = 11)
	private String userPhone;

	@Lob
	private String userPicture;

	@Size(max = 200)
	private String userAboutMe;

	@Enumerated(EnumType.STRING)
	private UserGender userGender;

	@NonNull
	private Boolean userTermsAgreed;

	@NonNull
	private Boolean userIsRemoved;

	private Double userLatitude;

	private Double userLongitude;

	@Column(length = 255)
	private String userAddress;

	@OneToMany // 단방향 1:N 매핑
	@JoinColumn(name = "dog_id")
	private List<Dog> dogs = new ArrayList<>();

	// UserDetails 로서의 User 속성 및 메소드들
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority(userRole.getValue()));
		return authorities;
	}

	@Override
	public String getUsername() {
		return userLoginId;
	}

	@Override
	public String getPassword() {
		return userPw;
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

	public static final class UserBuilder {
		private LocalDateTime createdDate;
		private LocalDateTime modifiedDate;
		private Long userId;
		private @Email @NonNull String userLoginId;
		private @NonNull String userPw;
		private @NonNull String userNickname;
		private UserRole userRole;
		private @NonNull @Size(max = 11) String userPhone;
		private String userPicture;
		private @Size(max = 200) String userAboutMe;
		private UserGender userGender;
		private @NonNull Boolean userTermsAgreed;
		private @NonNull Boolean userIsRemoved;
		private Double userLatitude;
		private Double userLongitude;
		private String userAddress;
		private List<Dog> dogs;

		private UserBuilder() {
		}

		public static UserBuilder anUser() {
			return new UserBuilder();
		}

		public UserBuilder withCreatedDate(LocalDateTime createdDate) {
			this.createdDate = createdDate;
			return this;
		}

		public UserBuilder withModifiedDate(LocalDateTime modifiedDate) {
			this.modifiedDate = modifiedDate;
			return this;
		}

		public UserBuilder withUserId(Long userId) {
			this.userId = userId;
			return this;
		}

		public UserBuilder withUserLoginId(String userLoginId) {
			this.userLoginId = userLoginId;
			return this;
		}

		public UserBuilder withUserPw(String userPw) {
			this.userPw = userPw;
			return this;
		}

		public UserBuilder withUserNickname(String userNickname) {
			this.userNickname = userNickname;
			return this;
		}

		public UserBuilder withUserRole(UserRole userRole) {
			this.userRole = userRole;
			return this;
		}

		public UserBuilder withUserPhone(String userPhone) {
			this.userPhone = userPhone;
			return this;
		}

		public UserBuilder withUserPicture(String userPicture) {
			this.userPicture = userPicture;
			return this;
		}

		public UserBuilder withUserAboutMe(String userAboutMe) {
			this.userAboutMe = userAboutMe;
			return this;
		}

		public UserBuilder withUserGender(UserGender userGender) {
			this.userGender = userGender;
			return this;
		}

		public UserBuilder withUserTermsAgreed(Boolean userTermsAgreed) {
			this.userTermsAgreed = userTermsAgreed;
			return this;
		}

		public UserBuilder withUserIsRemoved(Boolean userIsRemoved) {
			this.userIsRemoved = userIsRemoved;
			return this;
		}

		public UserBuilder withUserLatitude(Double userLatitude) {
			this.userLatitude = userLatitude;
			return this;
		}

		public UserBuilder withUserLongitude(Double userLongitude) {
			this.userLongitude = userLongitude;
			return this;
		}

		public UserBuilder withUserAddress(String userAddress) {
			this.userAddress = userAddress;
			return this;
		}

		public UserBuilder withDogs(List<Dog> dogs) {
			this.dogs = dogs;
			return this;
		}

		public User build() {
			User user = new User();
			user.userNickname = this.userNickname;
			user.userLatitude = this.userLatitude;
			user.userLoginId = this.userLoginId;
			user.userRole = this.userRole;
			user.userPhone = this.userPhone;
			user.userGender = this.userGender;
			user.userLongitude = this.userLongitude;
			user.userPicture = this.userPicture;
			user.dogs = this.dogs;
			user.userPw = this.userPw;
			user.userId = this.userId;
			user.userTermsAgreed = this.userTermsAgreed;
			user.userAddress = this.userAddress;
			user.userAboutMe = this.userAboutMe;
			user.userIsRemoved = this.userIsRemoved;
			return user;
		}
	}

	// === 연결 === //
	@OneToMany(mappedBy = "user")
	private List<Board> boardList = new ArrayList<>();

	@OneToMany(mappedBy = "user")
	private List<Comment> commentListForUser = new ArrayList<>();
}
