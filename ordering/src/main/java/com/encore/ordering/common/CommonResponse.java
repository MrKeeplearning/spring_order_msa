package com.encore.ordering.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class CommonResponse {
    // created나 updated된 경우 주로 사용
    private HttpStatus status;
    private String message;
    private Object result;
}
