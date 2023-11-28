package com.ssafy.dog.domain.email.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import java.time.Instant;

@Entity
@Getter
@NoArgsConstructor
public class VerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email
    private String email;

    private String token;

    private Instant expiryDate;

    public VerificationToken(String email, String token, Instant expiryDate) {
        this.email = email;
        this.token = token;
        this.expiryDate = expiryDate;
    }
}
