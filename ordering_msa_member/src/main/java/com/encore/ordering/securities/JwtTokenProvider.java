package com.encore.ordering.securities;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JwtTokenProvider {

    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private Long expiration;

    public String createToken(String email, String role) {
        // claims: 클레임은 토큰 사용자에 대한 속성이나 데이터 포함.
        // pk 값을 세팅하는 것처럼 이메일은 아래와 같이 작성한다(관례임!)
        // Claims claims = Jwts.claims()를 한 뒤, claims.put("email", email);과 같이 작성도 가능하다.
        Claims claims = Jwts.claims().setSubject(email);

        log.debug("expiration " + expiration);
        log.debug("secretKey " + secretKey);

        claims.put("role", role);
        Date now = new Date();
        // 빌더 패턴을 아래와 같이 작성하는 것도 가능하다.
//        JwtBuilder jwtBuilder = Jwts.builder();
//        jwtBuilder.setClaims(claims);
//        jwtBuilder.setIssuedAt(now);
//        jwtBuilder.setExpiration(new Date(now.getTime() + 30*60*1000L));
//        jwtBuilder.signWith(SignatureAlgorithm.HS256, "mysecret");
//        return jwtBuilder.compact();    // compact()는 builder().build()와 같이 build를 뜻한다.
        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + TimeUnit.MINUTES.toMillis(expiration)))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
        return token;
    }
}
