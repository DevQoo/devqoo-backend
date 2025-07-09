package com.devqoo.backend.auth.jwt;

import com.devqoo.backend.auth.dto.response.TokenResponseDto;
import com.devqoo.backend.auth.security.CustomUserDetails2;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@RequiredArgsConstructor
public class JwtLoginSuccessHandler implements AuthenticationSuccessHandler {

    private final ObjectMapper objectMapper;
    private final JwtService jwtService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        CustomUserDetails2 principal = (CustomUserDetails2) authentication.getPrincipal();
        // JWT 토큰 발급
        TokenResponseDto tokenResponseDto = jwtService.registerJwtToken(principal.userDto());

        // 헤더 쿠키에는 리프레시 토큰
        Cookie refreshTokenCookie = new Cookie(JwtService.REFRESH_TOKEN_COOKIE_NAME, tokenResponseDto.refreshToken());
        refreshTokenCookie.setHttpOnly(true);
        response.addCookie(refreshTokenCookie);

        // 응답 바디에는 액세스 토큰
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        // ACCESS 토큰 값만 응답 바디에 담아서 반환
        response.getWriter().write(objectMapper.writeValueAsString(tokenResponseDto.accessToken()));
    }
}
