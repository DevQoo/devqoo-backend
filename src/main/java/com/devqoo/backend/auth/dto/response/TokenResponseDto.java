package com.devqoo.backend.auth.dto.response;

public record TokenResponseDto(
    String accessToken,
    String refreshToken,
    int refreshExpireTime
) {

}
