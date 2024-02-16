package com.encore.ordering.common;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    @Bean
    // 유레카에 등록된 서비스명을 사용해서, 내부 서비스를 호출할 수 있게 해주는 어노테이션이다.
    // 따라서, TestContorller에서 localhost:8080으로 시작하는 것이 아닌 http://member-service/ 로 접근할 수 있는 것이다.
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
