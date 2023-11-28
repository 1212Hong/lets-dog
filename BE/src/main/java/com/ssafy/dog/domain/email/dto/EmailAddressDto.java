package com.ssafy.dog.domain.email.dto;

import lombok.Getter;

import javax.validation.constraints.Email;

@Getter
public class EmailAddressDto {
    @Email
    private String email;
}
