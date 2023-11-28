package com.ssafy.dog.domain.email.service;

import com.ssafy.dog.common.api.Api;
import com.ssafy.dog.domain.email.dto.EmailAddressDto;

public interface EmailService {
    void sendSimpleMessage(String to, String subject, String text);

    Api<?> sendVerificationEmail(EmailAddressDto emailAddressDto);

    void sendVerificationMessage(String to, String subject, String verificationCode);
}
