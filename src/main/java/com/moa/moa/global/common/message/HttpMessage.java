package com.moa.moa.global.common.message;

import org.springframework.http.HttpStatus;

public interface HttpMessage {
    HttpStatus getStatus();
    String getMessage();
}
