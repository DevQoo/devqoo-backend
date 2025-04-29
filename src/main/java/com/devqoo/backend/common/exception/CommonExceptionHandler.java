package com.devqoo.backend.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CommonExceptionHandler {

    // controller parameter 에 @Valid 실패 시
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    // 지원하지 않는 HTTP 메소드 요청 시
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<Void> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException exception) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
    }

    // 서버 예외 핸들링
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Void> handleException(Exception exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
