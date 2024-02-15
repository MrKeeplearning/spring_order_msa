package com.encore.ordering.securities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
//@EnableGlobalAuthentication   // 17버전 사용 시 위의 것으로 진행!
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final JwtAuthFilter authFilter;

    @Autowired
    public SecurityConfig(JwtAuthFilter authFilter) {
        this.authFilter = authFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    // 필터 체인 구성
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf().disable()
                .cors().and()   // CORS 활성화
                .httpBasic().disable()
                .authorizeHttpRequests()
                    .antMatchers("/member/create", "/doLogin", "/items", "/item/*/image")
//                .requestMatchers("/member/create", "/doLogin", "/items", "/items/image/**")   // 17버전 사용 시 적용
                    .permitAll()
                .anyRequest().authenticated()
                .and()
                // 세션을 사용하지 않겠다라는 설정을 추가
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // authFilter를 먼저 실행한다.
                // 그 뒤에 UsernamePasswordAuthenticationFilter를 거친다. 이 필터는 form 로그인 형식에서 사용한다.
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
