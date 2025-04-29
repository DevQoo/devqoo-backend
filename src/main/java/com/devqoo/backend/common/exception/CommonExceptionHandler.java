package com.devqoo.backend.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class CommonExceptionHandler {

    // controller parameter 에 @Valid 실패 시
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    // 쿼리스트링/PathVariable → 타입 매핑 실패(enum, 숫자 등)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<Void> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    // JSON·XML 본문 파싱 실패
    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<Void> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    // 필수 파라미터 누락
    @ExceptionHandler(MissingServletRequestParameterException.class)
    protected ResponseEntity<Void> handleMissingServletRequestParameter(MissingServletRequestParameterException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    // 지원하지 않는 Content-Type
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    protected ResponseEntity<Void> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex) {
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).build();
    }

    // 지원하지 않는 HTTP 메소드 요청 시
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<Void> handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException exception) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
    }

    // 서버 예외 핸들링
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Void> handleException(Exception exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
