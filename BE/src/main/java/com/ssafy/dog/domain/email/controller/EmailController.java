package com.ssafy.dog.domain.email.controller;

import com.ssafy.dog.common.api.Api;
import com.ssafy.dog.domain.email.dto.EmailAddressDto;
import com.ssafy.dog.domain.email.dto.EmailInputReq;
import com.ssafy.dog.domain.email.service.EmailService;
import com.ssafy.dog.domain.email.service.VerificationTokenService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RequestMapping("/api/email")
@RequiredArgsConstructor
@RestController
@Slf4j
public class EmailController {
    private final EmailService emailService;
    private final VerificationTokenService verificationTokenService;

    @PostMapping("/verification-email")
    public Api<?> sendVerificationEmail(@RequestBody EmailAddressDto emailAddressDto) {
        // 인증 코드 생성
        String verificationCode = verificationTokenService.createVerificationToken(emailAddressDto.getEmail());
        // 이메일 전송
        emailService.sendSimpleMessage(
                emailAddressDto.getEmail(),
                "[같이가개] 이메일 인증코드",
                "Your verification code is: " + verificationCode);

        Map<String, String> ret = new HashMap<>();
        ret.put("email", emailAddressDto.getEmail());
        return Api.ok(ret);
    }

    @PutMapping("/verification-code")
    @Operation(summary = "result 의 200, 성공 은 단순히 해당 API 가 잘 동작했다는 것. 인증 성공/실패 여부는 body 의 succeed 를 볼것.")
    public Api<Map<String, Boolean>> verifyCode(@RequestBody EmailInputReq emailInputReq) {
        boolean isVerified = verificationTokenService.verifyToken(emailInputReq.getToken(), emailInputReq.getEmail());
        Map<String, Boolean> ret = new HashMap<>();
        if (isVerified) {
            ret.put("succeed", Boolean.TRUE);
        } else {
            ret.put("succeed", Boolean.FALSE);
        }
        return Api.ok(ret);
    }

//    @PutMapping("/sendtest")
//    @Operation(summary = "이메일 보내기 테스트")
//    public Api<String> sendTest(@Valid @RequestBody EmailAddressDto emailAddressDto) {
//        emailService.sendSimpleMessage(emailAddressDto.getEmail(), "noreply@gmail.com", "테스트 메일입니다.");
//
//        return Api.ok("이메일 보내기 테스트 성공");
//    }
}
