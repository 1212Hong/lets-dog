package com.ssafy.dog.security.controller;

import com.ssafy.dog.common.api.Api;
import com.ssafy.dog.common.error.ErrorCode;
import com.ssafy.dog.common.exception.ApiException;
import com.ssafy.dog.security.JwtTokenProvider;
import com.ssafy.dog.security.dto.ValidRes;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Base64;

@RequestMapping("/jwt")
@RestController
@Slf4j
public class JwtController {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @GetMapping("/valid") //
    public Api<Object> valid(HttpServletRequest request) {
        String tokenHeader = request.getHeader("Authorization");

        if (tokenHeader != null && tokenHeader.startsWith("Bearer ")) {
            String jwt = tokenHeader.substring(7);

            if (jwtTokenProvider.validateToken(jwt)) {
                String[] chunks = jwt.split("\\.");

                Base64.Decoder decoder = Base64.getUrlDecoder();

                String header = new String(decoder.decode(chunks[0]));
                String payload = new String(decoder.decode(chunks[1]));

                JSONObject parser = new JSONObject(payload);

                Long userId = parser.getLong("sub");

                log.info("DEBUG : Header == " + header + ", Payload == " + payload);
                return Api.ok(new ValidRes(userId));
            }
        }

        throw new ApiException(ErrorCode.FORBIDDEN);
    }
}
