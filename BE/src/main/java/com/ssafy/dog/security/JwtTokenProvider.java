package com.ssafy.dog.security;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.ssafy.dog.domain.user.entity.User;
import com.ssafy.dog.domain.user.repository.UserRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtTokenProvider {

	private final Key key;
	private final long tokenValidTime = 60 * 60 * 24 * 365 * 1000L; // 1년
	private final UserRepository userRepository;

	public JwtTokenProvider(@Value("${jwt.secret}") String secretKey, UserRepository userRepository) {
		byte[] secretByteKey = DatatypeConverter.parseBase64Binary(secretKey);
		this.key = Keys.hmacShaKeyFor(secretByteKey);
		this.userRepository = userRepository;
	}

	public JwtToken generateToken(Authentication authentication) {
		String authorities = authentication.getAuthorities().stream()
			.map(GrantedAuthority::getAuthority)
			.collect(Collectors.joining(","));

		// String userLoginId = authentication.getName();

		Optional<User> curUser = userRepository.findByUserLoginId(authentication.getName());
		if (curUser.isEmpty()) {
			throw new UsernameNotFoundException("JwtTokenProvider 에서 authentication 에 담긴 userLoginId 로 유저를 찾을 수 없습니다.");
		}

		// Access Token 생성
		String accessToken = Jwts.builder()
			.setSubject(curUser.get().getUserId().toString())
			.claim("auth", authorities)
			.setExpiration(new Date(System.currentTimeMillis() + tokenValidTime))
			.signWith(key, SignatureAlgorithm.HS256)
			.compact();

		// RefreshToken은 AccessToken이 만료됐을 때 보내는거니까 안 만들어줘도 된다.

		return JwtToken.builder()
			.grantType("Bearer")
			.accessToken(accessToken)
			.build();
	}

	public Authentication getAuthentication(String accessToken) {
		Claims claims = parseClaims(accessToken);

		if (claims.get("auth") == null) { // generateToken() 에서 새긴 "auth"가 JWT 의 Payload 안의 claims 에 있는지 확인
			throw new RuntimeException("권한 정보가 없는 토큰입니다.");
		}

		Collection<? extends GrantedAuthority> authorities =
			Arrays.stream(claims.get("auth").toString().split(","))
				.map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());

		UserDetails principal = new org.springframework.security.core.userdetails.User(
			claims.getSubject(), "", authorities);

		return new UsernamePasswordAuthenticationToken(principal, "", authorities);
	}

	public boolean validateToken(String token) { // 에러 내보고 ApiException 으로 오류 처리해야 하는지 확인하기
		try {
			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
			return true;
		} catch (SecurityException | MalformedJwtException e) {
			log.info("Invalid JWT Token", e);
		} catch (ExpiredJwtException e) {
			log.info("Expired JWT Token", e);
		} catch (UnsupportedJwtException e) {
			log.info("Unsupported JWT Token", e);
		} catch (IllegalArgumentException e) {
			log.info("JWT claims string is empty.", e);
		}
		return false;
	}

	private Claims parseClaims(String accessToken) {
		try {
			return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
		} catch (ExpiredJwtException e) {
			return e.getClaims();
		}
	}
}
