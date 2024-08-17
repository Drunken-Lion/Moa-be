package com.moa.moa.global.exception;

import com.moa.moa.global.common.message.FailHttpMessage;
import com.moa.moa.global.common.message.HttpMessage;
import lombok.*;
import org.springframework.http.HttpStatus;

/**
 * 400번대 에러를 처리하기 위한 예외 클래스.
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BusinessException extends RuntimeException {
    private HttpStatus status;
    private String message;

    public BusinessException(HttpMessage response) {
        super(response.getMessage());
        this.status = response.getStatus();
        this.message = response.getMessage();
    }
}