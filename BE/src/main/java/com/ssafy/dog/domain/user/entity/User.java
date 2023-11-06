package com.ssafy.dog.domain.user.entity;

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
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

import org.hibernate.annotations.DynamicUpdate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.ssafy.dog.common.auditing.BaseTimeEntity;
import com.ssafy.dog.domain.board.entity.Board;
import com.ssafy.dog.domain.board.entity.Comment;
import com.ssafy.dog.domain.dog.entity.Dog;
import com.ssafy.dog.domain.user.dto.response.UserUpdateRes;
import com.ssafy.dog.domain.user.model.UserGender;
import com.ssafy.dog.domain.user.model.UserRole;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@Getter
@DynamicUpdate
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

	// === 연결 === //
	@OneToMany(mappedBy = "user")
	private List<Board> boardList = new ArrayList<>();

	@OneToMany(mappedBy = "user")
	private List<Comment> commentListForUser = new ArrayList<>();

	@OneToMany(mappedBy = "user")
	private List<Dog> dogs = new ArrayList<>();

	// === update 메소드 === //
	public void updateUser(String userNickname, String userPhone, String userPicture, String userAboutMe,
		UserGender userGender, Double userLatitude, Double userLongitude, String userAddress) {
		this.userNickname = userNickname;
		this.userPhone = userPhone;
		this.userPicture = userPicture;
		this.userAboutMe = userAboutMe;
		this.userGender = userGender;
		this.userLatitude = userLatitude;
		this.userLongitude = userLongitude;
		this.userAddress = userAddress;
	}

	public UserUpdateRes toUserUpdateRes() {
		return UserUpdateRes.builder()
			.userId(userId)
			.userLoginId(userLoginId)
			.userNickname(userNickname)
			.userPhone(userPhone)
			.userPicture(userPicture)
			.userAboutMe(userAboutMe)
			.userGender(userGender)
			.userLatitude(userLatitude)
			.userLongitude(userLongitude)
			.userAddress(userAddress)
			.build();
	}

	// === 빌더 === //
	public static final class UserBuilder {
		private Long userId;
		private String userLoginId;
		private String userPw;
		private String userNickname;
		private UserRole userRole;
		private String userPhone;
		private String userPicture;
		private String userAboutMe;
		private UserGender userGender;
		private Boolean userTermsAgreed;
		private Boolean userIsRemoved;
		private Double userLatitude;
		private Double userLongitude;
		private String userAddress;
		private List<Board> boardList;
		private List<Comment> commentListForUser;

		private UserBuilder() {
		}

		public static UserBuilder anUser() {
			return new UserBuilder();
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

		public UserBuilder withBoardList(List<Board> boardList) {
			this.boardList = boardList;
			return this;
		}

		public UserBuilder withCommentListForUser(List<Comment> commentListForUser) {
			this.commentListForUser = commentListForUser;
			return this;
		}

		public User build() {
			User user = new User();
			user.userPhone = this.userPhone;
			user.userLoginId = this.userLoginId;
			user.userNickname = this.userNickname;
			user.userRole = this.userRole;
			user.commentListForUser = this.commentListForUser;
			user.userPw = this.userPw;
			user.userAboutMe = this.userAboutMe;
			user.userPicture = this.userPicture;
			user.boardList = this.boardList;
			user.userAddress = this.userAddress;
			user.userLatitude = this.userLatitude;
			user.userLongitude = this.userLongitude;
			user.userGender = this.userGender;
			user.userTermsAgreed = this.userTermsAgreed;
			user.userIsRemoved = this.userIsRemoved;
			user.userId = this.userId;
			return user;
		}
	}
}
