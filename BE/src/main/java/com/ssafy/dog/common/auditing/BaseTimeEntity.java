package com.ssafy.dog.common.auditing;

import java.time.LocalDateTime;
import java.time.ZoneId;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseTimeEntity {

	@CreatedDate
	private LocalDateTime createdDate;

	@LastModifiedDate
	private LocalDateTime modifiedDate;

	// Constructor
	public BaseTimeEntity() {
		this.createdDate = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
		this.modifiedDate = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
	}
}
