package com.devqoo.backend.auth.jwt;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

@RequiredArgsConstructor
public class JwtLogoutHandler implements LogoutHandler {

    private final JwtService jwtService;
    private final TokenExtractor tokenExtractor;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        resolveRefreshToken(request)
            .ifPresent(refreshToken -> {
                // DB에 저장한 리프레시 토큰 무효화, AccessToken 은 블랙리스트에 저장
                String accessToken = tokenExtractor.extractJwtFromHeader(request);
                jwtService.invalidateJwtToken(accessToken, refreshToken);
                // 쿠키에서 리프레시 토큰 제거
                invalidateRefreshTokenCookie(response);
            });
    }

    private Optional<String> resolveRefreshToken(HttpServletRequest request) {
        return Arrays.stream(request.getCookies())
            .filter(cookie -> cookie.getName().equals(JwtService.REFRESH_TOKEN_COOKIE_NAME))
            .findFirst()
            .map(Cookie::getValue);
    }

    private void invalidateRefreshTokenCookie(HttpServletResponse response) {
        Cookie refreshTokenCookie = new Cookie(JwtService.REFRESH_TOKEN_COOKIE_NAME, "");
        refreshTokenCookie.setMaxAge(0);
        refreshTokenCookie.setHttpOnly(true);
        response.addCookie(refreshTokenCookie);
    }
}
