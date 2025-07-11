package com.devqoo.backend.auth.jwt;

import com.devqoo.backend.common.exception.BusinessException;
import com.devqoo.backend.common.exception.ErrorCode;
import com.devqoo.backend.user.enums.UserRoleType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtProvider {

    private final SecretKey accessKey;
    private final SecretKey refreshKey;
    private final int accessExpireTime;
    private final int refreshExpireTime;

    public JwtProvider(
        @Value("${JWT_ACCESS_SECRET}") String accessKey,
        @Value("${JWT_REFRESH_SECRET}") String refreshKey,
        @Value("${JWT_ACCESS_TIME}") int accessExpireTime,
        @Value("${JWT_REFRESH_TIME}") int refreshExpireTime
    ) {
        this.accessKey = Keys.hmacShaKeyFor(accessKey.getBytes(StandardCharsets.UTF_8));
        this.refreshKey = Keys.hmacShaKeyFor(refreshKey.getBytes(StandardCharsets.UTF_8));
        this.accessExpireTime = accessExpireTime;
        this.refreshExpireTime = refreshExpireTime;
    }

    // Access Token 발급
    public String generateAccessToken(Long userId, String email, UserRoleType role) {
        return createToken(userId, email, role, accessExpireTime, accessKey);
    }

    // Refresh Token 발급
    public String generateRefreshToken(Long userId, String email, UserRoleType role) {
        return createToken(userId, email, role, refreshExpireTime, refreshKey);
    }

    public Long getUserId(String token) {
        Claims claims = parseClaims(token, SecretKeyType.ACCESS);
        try {
            return Long.parseLong(claims.getSubject());
        } catch (NumberFormatException e) {
            throw new BusinessException(ErrorCode.JWT_IllegalArgument);
        }
    }

    public UserRoleType getUserRole(String token) {
        Claims claims = parseClaims(token, SecretKeyType.ACCESS);
        return claims.get("role", UserRoleType.class);
    }

    public String getUserEmail(String token) {
        Claims claims = parseClaims(token, SecretKeyType.ACCESS);
        return claims.get("email", String.class);
    }

    // Token 생성
    private String createToken(
        Long userId, String email, UserRoleType role, int expireTime, SecretKey secretKey
    ) {

        Instant now = Instant.now();
        Instant expiration = now.plusSeconds(expireTime);

        return Jwts.builder()
            .subject(userId.toString())
            .claim("email", email)
            .claim("role", role)
            .issuedAt(Date.from(now))
            .expiration(Date.from(expiration))
            .signWith(secretKey)
            .compact();
    }

    // Token 유효 확인
    public boolean validateToken(String token, SecretKeyType secretKeyType) {
        boolean verified;
        try {
            parseClaims(token, secretKeyType);
            verified = true;
        } catch (BusinessException exception) {
            log.error(exception.getMessage());
            verified = false;
        }
        return verified;
    }

    // 토큰의 남은 시간 추출
    public Long getRemainingTime(String token, SecretKeyType secretKeyType) {

        Claims claims = parseClaims(token, secretKeyType);

        return Math.max(Duration.between(Instant.now(), claims.getExpiration().toInstant()).getSeconds(), 0);
    }

    // Claims 의 정보 확인
    private Claims parseClaims(String token, SecretKeyType secretKeyType) {

        try {

            return Jwts.parser()
                .verifyWith(getSecretKeyByType(secretKeyType))
                .build()
                .parseSignedClaims(token)
                .getPayload();

        } catch (ExpiredJwtException ex) {
            throw new BusinessException(ErrorCode.JWT_EXPIRED);
        } catch (SignatureException ex) {
            throw new BusinessException(ErrorCode.JWT_SIGNATURE);
        } catch (MalformedJwtException ex) {
            throw new BusinessException(ErrorCode.JWT_MALFORMED);
        } catch (UnsupportedJwtException ex) {
            throw new BusinessException(ErrorCode.JWT_UNSUPPORTED);
        } catch (IllegalArgumentException ex) {
            throw new BusinessException(ErrorCode.JWT_IllegalArgument);
        }
    }

    // SecretKeyType 값 반환
    private SecretKey getSecretKeyByType(SecretKeyType secretKeyType) {
        return switch (secretKeyType) {
            case ACCESS -> accessKey;
            case REFRESH -> refreshKey;
        };
    }
}
