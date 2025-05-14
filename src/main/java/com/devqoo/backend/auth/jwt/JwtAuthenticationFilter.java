package com.devqoo.backend.auth.jwt;

import static com.devqoo.backend.auth.jwt.SecretKeyType.ACCESS;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtTProvider;

    @Override
    protected void doFilterInternal(
        HttpServletRequest request, HttpServletResponse response, FilterChain filterChain
    ) throws ServletException, IOException {

        String token = extractJwtFromHeader(request);

        if (jwtTProvider.validateToken(token, ACCESS)) {

            Authentication authentication = jwtTProvider.getAuthentication(token, ACCESS);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    // Authorization 헤더에서 Token 추출
    private String extractJwtFromHeader(HttpServletRequest request) {

        String header = request.getHeader("Authorization");

        String bearer = "Bearer ";
        if (StringUtils.hasText(header) && header.startsWith(bearer)) {
            return header.substring(bearer.length());
        }

        return null;
    }
}
