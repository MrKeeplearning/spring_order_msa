package com.encore.ordering.securities;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JwtGlobalFilter implements GlobalFilter {

    @Value("${jwt.secretKey}")
    private String secretKey;

    private final List<String> allowUrl = Arrays.asList("/member/create", "/doLogin", "/items", "/item/*/image");

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String reqUri = request.getURI().getPath();
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        boolean isAllowed = allowUrl.stream().anyMatch(uri -> antPathMatcher.match(uri, reqUri));
        if (isAllowed) {
            return chain.filter(exchange);
        }

        String bearerToken = request.getHeaders().getFirst("Authorization");

        try {
            if (bearerToken != null) {
                if (!bearerToken.startsWith("Bearer ")) {
                    throw new IllegalArgumentException("token의 형식이 맞지 않습니다.");
                }
                // bearerToken에서 토큰 값만 추출
                String token = bearerToken.replace("Bearer ", "");

                // 토큰 검증 및 claim 추출. parsing 하는 것 자체가 검증. 에러가 안터지면 검증완료
                Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
                String email = claims.getSubject();
                String role = claims.get("role", String.class);
                request = exchange.getRequest().mutate()
                        .header("myEmail", email)
                        .header("myRole", role)
                        .build();
                exchange = exchange.mutate().request(request).build();
            } else {
                throw new RuntimeException("token is empty");
            }
        } catch (Exception e) {
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
        return chain.filter(exchange);
    }
}
