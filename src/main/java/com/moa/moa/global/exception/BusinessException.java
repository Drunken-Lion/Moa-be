package com.moa.moa.global.exception;

import com.moa.moa.global.common.message.FailHttpMessage;
import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BusinessException extends RuntimeException {
    private HttpStatus status;
    private String message;

    public BusinessException(FailHttpMessage response) {
        super(response.getMessage());
        this.status = response.getStatus();
        this.message = response.getMessage();
    }
}