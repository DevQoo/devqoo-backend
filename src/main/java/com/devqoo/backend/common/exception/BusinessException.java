package com.devqoo.backend.common.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private final ErrorCode errorCode;

    // 생성자
    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());  // ErrorCode 메시지 자동 연결
        this.errorCode = errorCode;
    }

}
