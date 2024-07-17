package com.moa.moa.global.exception;

import com.moa.moa.global.common.HttpMessage;
import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BusinessException extends RuntimeException {
    private HttpStatus status;
    private String message;

    @Builder
    public BusinessException(HttpMessage.Fail response) {
        super(response.getMessage());
        this.status = response.getStatus();
        this.message = response.getMessage();
    }
}