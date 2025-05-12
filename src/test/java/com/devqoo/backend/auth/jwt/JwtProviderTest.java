package com.devqoo.backend.auth.jwt;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class JwtProviderTest {

    // given
    private final Long userId = 1L;
    private final String email = "test@test.com";
    private final String role = "USER";

    private JwtProvider jwtProvider;

    @BeforeEach
    void setUp() {

        String accessKey = "mock-access-secret-key-must-be-32bytes!!";
        String refreshKey = "mock-refresh-secret-key-must-be-32byte!!";

        jwtProvider = new JwtProvider(accessKey, refreshKey);

        ReflectionTestUtils.setField(jwtProvider, "ACCESS_EXPIRE_TIME", 300000);
        ReflectionTestUtils.setField(jwtProvider, "REFRESH_EXPIRE_TIME", 330000);
    }

    @Test
    void generateAccessToken() {

        // when
        String token = jwtProvider.generateAccessToken(userId, email, role);

        // then

        Claims claims = ReflectionTestUtils.invokeMethod(
            jwtProvider, "parseClaims", token, SecretKeyType.ACCESS
        );

        assertEquals(String.valueOf(userId), claims.getSubject());
        assertEquals(email, claims.get("email"));
        assertEquals(role, claims.get("role"));

        System.out.println("token: " + token);
        System.out.println("userId : " + claims.getSubject());
        System.out.println("email : " + claims.get("email"));
        System.out.println("role : " + claims.get("role"));
    }

    @Test
    void generateRefreshToken() {

        // when
        String token = jwtProvider.generateRefreshToken(userId, email, role);

        // then

        Claims claims = ReflectionTestUtils.invokeMethod(
            jwtProvider, "parseClaims", token, SecretKeyType.REFRESH
        );

        assertEquals(String.valueOf(userId), claims.getSubject());
        assertEquals(email, claims.get("email"));
        assertEquals(role, claims.get("role"));

        System.out.println("token: " + token);
        System.out.println("userId : " + claims.getSubject());
        System.out.println("email : " + claims.get("email"));
        System.out.println("role : " + claims.get("role"));
    }
}
