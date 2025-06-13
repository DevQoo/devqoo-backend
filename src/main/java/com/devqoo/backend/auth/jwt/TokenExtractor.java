package com.devqoo.backend.auth.jwt;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class TokenExtractor {

    private static final String BEARER = "Bearer ";


    // Authorization 헤더에서 Token 추출
     public String extractJwtFromHeader(HttpServletRequest request) {

        String header = request.getHeader("Authorization");

        if (StringUtils.hasText(header) && header.startsWith(BEARER)) {
            return header.substring(BEARER.length());
        }

        return null;
    }
}
