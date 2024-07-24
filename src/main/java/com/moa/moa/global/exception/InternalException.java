package com.moa.moa.global.exception;

import com.moa.moa.global.common.message.FailHttpMessage;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * 500번대 에러를 처리하기 위한 예외 클래스. <br><br>
 * Ex) 외부 API 통신 오류 및 Converter 오류
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class InternalException extends RuntimeException {
    private HttpStatus status;
    private String message;

    public InternalException(FailHttpMessage response) {
        super(response.getMessage());
        this.status = response.getStatus();
        this.message = response.getMessage();
    }
}