package com.devqoo.backend.auth.jwt;

import static com.devqoo.backend.auth.jwt.SecretKeyType.ACCESS;

import com.devqoo.backend.auth.repository.AuthRepository;
import com.devqoo.backend.common.exception.BusinessException;
import com.devqoo.backend.common.exception.ErrorCode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.PathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtTProvider;
    private final TokenExtractor tokenExtractor;
    private final AuthRepository authRepository;
    private final PathMatcher pathMatcher;

    private static final String[] API = {
        "/api/users",   // 회원가입
        "/api/auth"     // 로그인
    };

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {

        String path = request.getServletPath();
        return Arrays.stream(API).anyMatch(pattern -> pathMatcher.match(pattern, path));
    }

    @Override
    protected void doFilterInternal(
        HttpServletRequest request, HttpServletResponse response, FilterChain filterChain
    ) throws ServletException, IOException {

        try {

            String token = tokenExtractor.extractJwtFromHeader(request);

            jwtTProvider.validateToken(token, ACCESS);

            if (authRepository.isAccessTokenBlackList(token)) {
                throw new BusinessException(ErrorCode.JWT_BLACKLIST);
            }

            Authentication authentication = jwtTProvider.getAuthentication(token, ACCESS);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            filterChain.doFilter(request, response);

        } catch (BusinessException e) {
            request.setAttribute("exception", e.getErrorCode());
            throw new BadCredentialsException(e.getErrorCode().name());
        }
    }
}
