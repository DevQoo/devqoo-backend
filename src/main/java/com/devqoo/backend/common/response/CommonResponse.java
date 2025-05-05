package com.devqoo.backend.common.response;

import com.devqoo.backend.common.exception.ErrorCode;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public final class CommonResponse<T> {

    private final int statusCode;             // HTTP STATUS CODE
    private final T data;                     // response data
    private final String errorMessage;        // error message


    // ✅ success일 경우: data와 statusCode만 채움
    public static <T> CommonResponse<T> success(
        int statusCode,
        T data
    ) {
        return CommonResponse.<T>builder()
            .statusCode(statusCode)
            .data(data)
            .errorMessage("")
            .build();
    }

    // ✅ error일 경우: errorCode로부터 statusCode와 기본 메시지를 가져옴
    public static <T> CommonResponse<T> error(
        ErrorCode errorCode,
        String errorMessage
    ) {
        return CommonResponse.<T>builder()
            .statusCode(errorCode.getHttpStatus())
            .data(null)
            .errorMessage(errorMessage != null ? errorMessage : errorCode.getMessage())
            .build();
    }
}
