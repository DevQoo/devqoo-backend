package com.devqoo.backend.common.response;

import com.devqoo.backend.common.exception.ErrorCode;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "API 응답 공통 모델")
public final class CommonResponse<T> {

    private final LocalDateTime responseTime;           // 응답 시간
    private final int statusCode;                       // HTTP STATUS CODE
    private final T data;                               // response data
    private final List<String> errorMessageList;        // error message


    // ✅ success일 경우: data와 statusCode만 채움
    public static <T> CommonResponse<T> success(int statusCode, T data) {
        return CommonResponse.<T>builder()
            .responseTime(LocalDateTime.now())
            .statusCode(statusCode)
            .data(data)
            .errorMessageList(null)
            .build();
    }

    // ✅ error일 경우: errorCode로부터 statusCode와 기본 메시지 여러개일 경우
    public static <T> CommonResponse<T> error(ErrorCode errorCode, List<String> errorMessages) {
        return CommonResponse.<T>builder()
            .responseTime(LocalDateTime.now())
            .statusCode(errorCode.getHttpStatusCode())
            .data(null)
            .errorMessageList(errorMessages)
            .build();
    }

    // ✅ error일 경우: errorCode 하나일 경우
    public static <T> CommonResponse<T> error(ErrorCode errorCode) {
        return error(errorCode, List.of(errorCode.getMessage()));
    }
}
