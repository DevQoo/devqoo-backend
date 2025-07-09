package com.devqoo.backend.auth.jwt;

import com.devqoo.backend.auth.repository.AuthRepository;
import com.devqoo.backend.auth.security.CustomUserDetails;
import com.devqoo.backend.auth.security.SecurityMatchers;
import com.devqoo.backend.common.exception.ErrorCode;
import com.devqoo.backend.common.response.CommonResponse;
import com.devqoo.backend.user.dto.response.UserDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final String PREFIX = "Bearer ";

    private final ObjectMapper objectMapper;
    private final JwtProvider jwtTProvider;
    private final AuthRepository authRepository;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return Arrays.stream(SecurityMatchers.PUBLIC_MATCHERS)
            .anyMatch(requestMatcher -> requestMatcher.matches(request));
    }

    @Override
    protected void doFilterInternal(
        HttpServletRequest request, HttpServletResponse response, FilterChain filterChain
    ) throws ServletException, IOException {
        Optional<String> optionalAccessToken = resolveAccessJwtFromHeader(request);
        // 토큰이 없으면 다음 필터 호출
        if (optionalAccessToken.isPresent()) {
            String accessToken = optionalAccessToken.get();
            // 토큰이 유효한지 검사: 유효하지 않다면 response에 응답 데이터 담아서 반환합니다.
            if (
                jwtTProvider.validateToken(accessToken, SecretKeyType.ACCESS) &&
                    !authRepository.isAccessTokenBlackList(accessToken)
            ) {
                UserDto userDto = jwtTProvider.parseUserDto(accessToken, SecretKeyType.ACCESS);
                CustomUserDetails userDetails = new CustomUserDetails(userDto, null);
                UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                // 인증 정보 시큐리티 컨텍스트 저장
                SecurityContextHolder.getContext().setAuthentication(auth);
                filterChain.doFilter(request, response);
            } else {
                CommonResponse<Void> errorResponse = CommonResponse.error(ErrorCode.JWT_IllegalArgument);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }

    public Optional<String> resolveAccessJwtFromHeader(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION))
            .map(accessToken -> {
                if (accessToken.startsWith(PREFIX)) {
                    return accessToken.substring(PREFIX.length());
                } else {
                    return null;
                }
            });
    }
}
