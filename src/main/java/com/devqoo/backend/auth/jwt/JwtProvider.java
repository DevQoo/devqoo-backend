package com.devqoo.backend.auth.jwt;

import com.devqoo.backend.auth.security.CustomUserDetails;
import com.devqoo.backend.common.exception.BusinessException;
import com.devqoo.backend.common.exception.ErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class JwtProvider {

    private final SecretKey accessKey;
    private final SecretKey refreshKey;

    @Value("${JWT_ACCESS_TIME}")
    private int ACCESS_EXPIRE_TIME;
    @Value("${JWT_REFRESH_TIME}")
    private int REFRESH_EXPIRE_TIME;

    public JwtProvider(
        @Value("${JWT_ACCESS_SECRET}") String accessKey,
        @Value("${JWT_REFRESH_SECRET}") String refreshKey
    ) {
        this.accessKey = Keys.hmacShaKeyFor(accessKey.getBytes(StandardCharsets.UTF_8));
        this.refreshKey = Keys.hmacShaKeyFor(refreshKey.getBytes(StandardCharsets.UTF_8));
    }

    // Access Token 발급
    public String generateAccessToken(Long userId, String email, String role) {
        return createToken(userId, email, role, ACCESS_EXPIRE_TIME, accessKey);
    }

    // Refresh Token 발급
    public String generateRefreshToken(Long userId, String email, String role) {
        return createToken(userId, email, role, REFRESH_EXPIRE_TIME, refreshKey);
    }

    // Token 생성
    public String createToken(
        Long userId, String email, String role, int expireTime, SecretKey secretKey
    ) {

        Date now = new Date();

        return Jwts.builder()
            .subject(userId.toString())
            .claim("email", email)
            .claim("role", role)
            .issuedAt(now)
            .expiration(new Date(now.getTime() + expireTime))
            .signWith(secretKey)
            .compact();
    }

    // Token 유효 확인
    public boolean validateToken(String token, SecretKeyType secretKeyType) {

        try {

            Claims claims = parseClaims(token, secretKeyType);

            if (claims == null) {
                return false;
            }
            return !claims.getExpiration().before(new Date());

        } catch (Exception ex) {
            return false;
        }
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

    // JWT 토큰에서 사용자 정보를 추출하여 Spring Security 의 Authentication 객체로 변환
    public Authentication getAuthentication(String token, SecretKeyType secretKeyType) {

        Claims claims = parseClaims(token, secretKeyType);

        Long userId = Long.parseLong(claims.getSubject());
        String email = claims.get("email", String.class);
        String role = claims.get("role", String.class);

        CustomUserDetails customUserDetails =
            new CustomUserDetails(userId, email, role);

        return new UsernamePasswordAuthenticationToken(
            customUserDetails, "", List.of(new SimpleGrantedAuthority(role))
        );
    }
}
