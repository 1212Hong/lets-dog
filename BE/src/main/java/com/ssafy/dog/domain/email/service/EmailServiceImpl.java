package com.ssafy.dog.domain.email.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.internet.MimeMessage;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender emailSender;
    private final Environment env;
    private final TemplateEngine templateEngine;

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

    public void sendVerificationMessage(String to, String subject, String verificationCode) {
        Context context = new Context();
        context.setVariable("code", verificationCode); // Set the variable for the template

        // Processing the template
        String process = templateEngine.process("email", context);

        try {
            MimeMessage mimeMessage = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setFrom("noreply@gmail.com");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(process, true); // Set processed HTML as the Email content

            emailSender.send(mimeMessage);

            log.info("Email sent to: {}, subject: {}, text: {}", to, subject, verificationCode);
        } catch (Exception e) {
            log.error("Error sending email", e);
        }
    }
}
