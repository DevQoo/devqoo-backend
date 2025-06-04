package com.devqoo.backend.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // ======== JWT 오류 =========
    JWT_EXPIRED(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다."),
    JWT_SIGNATURE(HttpStatus.UNAUTHORIZED, "서명이 잘못되었습니다."),
    JWT_MALFORMED(HttpStatus.BAD_REQUEST, "잘못된 형식의 토큰입니다."),
    JWT_UNSUPPORTED(HttpStatus.BAD_REQUEST, "지원하지 않는 구조의 토큰입니다."),
    JWT_IllegalArgument(HttpStatus.BAD_REQUEST, "토큰이 비어 있거나 잘못되었습니다."),

    // ======== 공통 오류 =========
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "입력값이 유효하지 않습니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "허용되지 않은 HTTP 메서드입니다."),
    UNSUPPORTED_MEDIA_TYPE(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "지원하지 않는 Content-Type입니다."),
    MISSING_PARAMETER(HttpStatus.BAD_REQUEST, "필수 요청 파라미터가 누락되었습니다."),
    TYPE_MISMATCH(HttpStatus.BAD_REQUEST, "요청 파라미터 타입이 올바르지 않습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버에 오류가 발생했습니다."),
    INVALID_ENUM_VALUE(HttpStatus.BAD_REQUEST, "유효하지 않은 열거형 값입니다."),

    // ======== 비즈니스 오류 =========
    // ======== 게시글 =========
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다."),
    USER_UNAUTHORIZED(HttpStatus.FORBIDDEN, "권한이 없습니다."),

    // ========= 카테고리 ============
    CATEGORY_NAME_DUPLICATED(HttpStatus.CONFLICT, "카테고리 이름이 중복되었습니다."),
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "카테고리를 찾을 수 없습니다."),

    // ======== 유저 =========
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "아이디 또는 비밀번호가 잘못 되었습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다."),
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다."),
    NICKNAME_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 닉네임입니다."),

    // ======== 댓글 =========
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "댓글을 찾을 수 없습니다."),
    
    // ======== 레디스 =========
    REDIS_EXCEPTION(
        HttpStatus.INTERNAL_SERVER_ERROR, "서버에 문제가 발생했습니다. 잠시 후 다시 시도해주세요.");


    private final HttpStatus httpStatus;    // HTTP STATUS CODE
    private final String message;           // error message


    // StatusCode int로 반환
    public int getHttpStatusCode() {
        return httpStatus.value();
    }
}
