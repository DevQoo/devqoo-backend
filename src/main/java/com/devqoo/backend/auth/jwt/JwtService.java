package com.devqoo.backend.auth.jwt;

import com.devqoo.backend.auth.dto.response.TokenResponseDto;
import com.devqoo.backend.auth.repository.AuthRepository;
import com.devqoo.backend.common.exception.BusinessException;
import com.devqoo.backend.common.exception.ErrorCode;
import com.devqoo.backend.user.dto.response.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtService {
    public static final String REFRESH_TOKEN_COOKIE_NAME = "refresh_token";

    private final JwtProvider jwtProvider;
    private final AuthRepository authRepository;   // Redis 토큰 저장 레포지토리

    @Value("${JWT_REFRESH_TIME}")
    private int refreshExpireTime;

    public TokenResponseDto registerJwtToken(UserDto userDto) {
        String refreshToken = jwtProvider.generateRefreshToken(userDto);
        try {
            authRepository.saveRefreshToken(userDto.userId(), refreshToken, refreshExpireTime);
        } catch (Exception ex) {
            throw new BusinessException(ErrorCode.REDIS_EXCEPTION);
        }
        String accessToken = jwtProvider.generateAccessToken(userDto);
        return new TokenResponseDto(accessToken, refreshToken, refreshExpireTime);
    }

    // 토큰 무효화
    public void invalidateJwtToken(String token) {
        Long remainingTime = jwtProvider.getRemainingTime(token, SecretKeyType.ACCESS);
        UserDto userDto = jwtProvider.parseUserDto(token, SecretKeyType.REFRESH);
        try {
            authRepository.saveBlackList(token, remainingTime);
            authRepository.removeRefreshToken(userDto.userId());
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.REDIS_EXCEPTION);
        }
    }
}
