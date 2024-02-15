package com.encore.ordering.securities;

import com.encore.ordering.common.ErrorResponseDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.FilterChain;
import javax.servlet.GenericFilter;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthFilter extends GenericFilter {


    @Value("${jwt.secretKey}")
    private String secretKey;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            String bearerToken = ((HttpServletRequest) request).getHeader("Authorization");

            if (bearerToken != null) {
                if (!bearerToken.startsWith("Bearer ")) {
                    throw new AuthenticationServiceException("token의 형식이 맞지 않습니다.");
                }
                // bearerToken에서 토큰 값만 추출
                String token = bearerToken.replace("Bearer ", "");

                // 토큰 검증 및 claim 추출. parsing 하는 것 자체가 검증
                // Body , PayLoader , Claims 모두 비슷한 성격을 가진 것
                Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();

                // Authentication 객체를 생성하기 위한 UserDetails 생성
                List<GrantedAuthority> authorities = new ArrayList<>();
                authorities.add(new SimpleGrantedAuthority("ROLE_" + claims.get("role")));
                UserDetails userDetails = new User(claims.getSubject(), "", authorities);

                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, "", userDetails.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            // filterchain에서 그 다음 filtering으로 넘어가도록 하는 메서드
            chain.doFilter(request, response);

        } catch (Exception e) {
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
            httpServletResponse.setContentType("application/json");
            httpServletResponse.getWriter().write(
                    String.valueOf(ErrorResponseDto.makeMessage(HttpStatus.UNAUTHORIZED, e.getMessage()))
            );
        }
    }
}
