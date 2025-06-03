package com.devqoo.backend.common.exception;

import static com.devqoo.backend.common.exception.ErrorCode.INTERNAL_SERVER_ERROR;
import static com.devqoo.backend.common.exception.ErrorCode.INVALID_ENUM_VALUE;
import static com.devqoo.backend.common.exception.ErrorCode.INVALID_INPUT_VALUE;
import static com.devqoo.backend.common.exception.ErrorCode.METHOD_NOT_ALLOWED;
import static com.devqoo.backend.common.exception.ErrorCode.MISSING_PARAMETER;
import static com.devqoo.backend.common.exception.ErrorCode.UNSUPPORTED_MEDIA_TYPE;

import com.devqoo.backend.common.response.CommonResponse;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@RestControllerAdvice
public class CommonExceptionHandler {

    // controller parameter 에 @Valid 실패 시
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<CommonResponse<Void>> handleMethodArgumentNotValidException(
        MethodArgumentNotValidException exception
    ) {
        log.warn("==========>>>> handleMethodArgumentNotValidException: {}", exception.getMessage());

        List<String> errorMessage = getErrorList(exception);

        return ResponseEntity
            .status(INVALID_INPUT_VALUE.getHttpStatusCode())
            .body(CommonResponse.error(INVALID_INPUT_VALUE, errorMessage));
    }

    // errorList 반환
    private static List<String> getErrorList(MethodArgumentNotValidException exception) {
        return exception.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(error -> String.format("[%s] %s", error.getField(), error.getDefaultMessage()))
            .toList();
    }

    // 쿼리스트링/PathVariable → 타입 매핑 실패(enum, 숫자 등)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<CommonResponse<Void>> handleMethodArgumentTypeMismatch(
        MethodArgumentTypeMismatchException exception
    ) {
        log.warn("==========>>>> handleMethodArgumentTypeMismatch: {}", exception.getMessage());

        // Enum 타입 변환 실패인 경우 → 공통 비즈니스 예외로 래핑
        if (exception.getRequiredType() != null && exception.getRequiredType().isEnum()) {
            return handleBusinessException(new BusinessException(INVALID_ENUM_VALUE));
        }

        // 그 외 일반적인 타입 불일치
        return ResponseEntity
            .status(ErrorCode.TYPE_MISMATCH.getHttpStatusCode())
            .body(CommonResponse.error(ErrorCode.TYPE_MISMATCH));
    }

    // JSON·XML 본문 파싱 실패
    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<CommonResponse<Void>> handleHttpMessageNotReadable(
        HttpMessageNotReadableException exception
    ) {
        log.warn("==========>>>> handleHttpMessageNotReadable: {}", exception.getMessage());

        return ResponseEntity
            .status(INVALID_INPUT_VALUE.getHttpStatusCode())
            .body(CommonResponse.error(INVALID_INPUT_VALUE));
    }

    // 필수 파라미터 누락
    @ExceptionHandler(MissingServletRequestParameterException.class)
    protected ResponseEntity<CommonResponse<Void>> handleMissingServletRequestParameter(
        MissingServletRequestParameterException exception
    ) {
        log.warn("==========>>>> handleMissingServletRequestParameter: {}", exception.getMessage());

        return ResponseEntity
            .status(MISSING_PARAMETER.getHttpStatusCode())
            .body(CommonResponse.error(MISSING_PARAMETER));
    }

    // 지원하지 않는 Content-Type
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    protected ResponseEntity<CommonResponse<Void>> handleHttpMediaTypeNotSupported(
        HttpMediaTypeNotSupportedException exception
    ) {
        log.warn("==========>>>> handleHttpMediaTypeNotSupported: {}", exception.getMessage());

        return ResponseEntity
            .status(UNSUPPORTED_MEDIA_TYPE.getHttpStatusCode())
            .body(CommonResponse.error(UNSUPPORTED_MEDIA_TYPE));
    }

    // 지원하지 않는 HTTP 메소드 요청 시
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<CommonResponse<Void>> handleHttpRequestMethodNotSupportedException(
        HttpRequestMethodNotSupportedException exception
    ) {
        log.warn("==========>>>> handleHttpRequestMethodNotSupportedException: {}", exception.getMessage());

        return ResponseEntity
            .status(METHOD_NOT_ALLOWED.getHttpStatusCode())
            .body(CommonResponse.error(METHOD_NOT_ALLOWED));
    }

    // 서버 예외 핸들링
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<CommonResponse<Void>> handleException(Exception exception) {
        log.warn("==========>>>> handleException: {}", exception.getMessage());

        return ResponseEntity
            .status(INTERNAL_SERVER_ERROR.getHttpStatusCode())
            .body(CommonResponse.error(INTERNAL_SERVER_ERROR));
    }

    // 비즈니스 예외 핸들링
    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<CommonResponse<Void>> handleBusinessException(BusinessException exception) {
        log.warn("==========>>>> handleBusinessException: {}", exception.getMessage());

        ErrorCode errorCode = exception.getErrorCode();
        return ResponseEntity
            .status(errorCode.getHttpStatusCode())
            .body(CommonResponse.error(errorCode));
    }
}
