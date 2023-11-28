package com.ssafy.dog.domain.email.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Email;

@Getter
@RequiredArgsConstructor
public class EmailInputReq {
    @Email
    private String email;

    private String token;
}
