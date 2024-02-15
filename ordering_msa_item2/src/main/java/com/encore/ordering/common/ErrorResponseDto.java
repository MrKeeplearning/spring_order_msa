package com.encore.ordering.common;

import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Data
@AllArgsConstructor
public class ErrorResponseDto {
    public static ResponseEntity<Map<String, Object>> makeMessage(HttpStatus status, String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("status", Integer.toString(status.value())); //responseBody에 출력되도록 넣기
        body.put("status message", status.getReasonPhrase());
        body.put("error_message", message); // frontend에서 에러메시지를 받을 때 문제가 되기 때문에 띄어쓰기는 사용하면 안된다.
        return new ResponseEntity<>(body, status); //status에 담긴 값은 header로 나가고, body에 담긴 Map은 json 형태로 나감.
    }
}
