package com.ssafy.dog.domain.email.service;

public interface EmailService {
    void sendSimpleMessage(String to, String subject, String text);
}
