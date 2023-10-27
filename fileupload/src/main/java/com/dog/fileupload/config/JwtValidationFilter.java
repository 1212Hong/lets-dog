package com.dog.fileupload.config;

import com.dog.fileupload.common.api.Api;
import com.dog.fileupload.common.error.ErrorCode;
import com.dog.fileupload.common.exception.ApiException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

@Component
@Slf4j
public class JwtValidationFilter implements WebFilter {

    private final WebClient webClient;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    public JwtValidationFilter(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://k9c205.p.ssafy.io:8000/jwt").build();
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String jwtToken = exchange.getRequest().getHeaders().getFirst("Authorization");

        String requestPath = exchange.getRequest().getPath().toString();

        if (pathMatcher.match("/swagger-ui/**", requestPath) ||
                pathMatcher.match("/v3/api-docs", requestPath) ||
                pathMatcher.match("/swagger-resources/**", requestPath)) {
            log.info("스웨커 필터 통과");
            return chain.filter(exchange);
        }

        if (jwtToken == null || jwtToken.isEmpty()) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        return webClient.get()
                .uri("/valid")
                .header("Authorization", jwtToken)
                .retrieve()
                .bodyToMono(Api.class)
                .flatMap(reponse -> {
                    String userPk = reponse.getBody().toString();
                    log.info("받은 토큰 정보 : {}", userPk);
                    exchange.getAttributes().put("userPk", userPk);
                    return chain.filter(exchange).contextWrite(Context.of("userPk", userPk));
                })
                .onErrorResume(e -> {
                    log.info("에러 : {}", e.toString());
                    try {
                        byte[] bytes = new ObjectMapper().writeValueAsBytes(
                                Api.error(ErrorCode.UNAUTHORIZED, "유효하지 않은 토큰"));
                        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
                        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);

                        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);

                        return exchange.getResponse().writeWith(Mono.just(buffer));
                    } catch (JsonProcessingException ex) {
                        throw new ApiException(ErrorCode.BAD_REQUEST);
                    }
                });
    }
}
