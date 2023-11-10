package com.ssafy.dog.domain.email.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender emailSender;
    private final Environment env;

    public void sendSimpleMessage(
            String to, String subject, String text) {

        String host = env.getProperty("spring.mail.host");
        String port = env.getProperty("spring.mail.port");
        String username = env.getProperty("spring.mail.username");
        String password = env.getProperty("spring.mail.password");
        String auth = env.getProperty("spring.mail.properties.mail.smtp.auth");
        String starttlsEnable = env.getProperty("spring.mail.properties.mail.smtp.starttls.enable");
        String protocol = env.getProperty("spring.mail.properties.mail.transport.protocol");

        log.info("spring.mail.host : {}", host);
        log.info("spring.mail.port : {}", port);
        log.info("spring.mail.username : {}", username);
        log.info("spring.mail.password : {}", password);
        log.info("spring.mail.properties.mail.smtp.auth : {}", auth);
        log.info("spring.mail.properties.mail.smtp.starttls.enables : {}", starttlsEnable);
        log.info("spring.mail.properties.mail.transport.protocol : {}", protocol);

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("noreply@gmail.com");
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            emailSender.send(message);

            log.info("Email sent to: {}, subject: {}, text: {}", to, subject, text);
        } catch (Exception e) {
            log.error("Error sending email", e);
        }

    }
}
