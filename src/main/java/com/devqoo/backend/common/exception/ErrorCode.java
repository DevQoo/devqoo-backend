package com.devqoo.backend.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "잘못된 요청입니다.");


    private final HttpStatus httpStatus;    // HTTP STATUS CODE
    private final String message;           // error message


    /* 생성자 */
    ErrorCode(
        HttpStatus httpStatus,
        String message
    ) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    /* Status int로 반환 */
    public int getHttpStatus() {
        return httpStatus.value();
    }
}
