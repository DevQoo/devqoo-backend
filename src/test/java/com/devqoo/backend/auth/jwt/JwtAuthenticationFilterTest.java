package com.devqoo.backend.auth.jwt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.devqoo.backend.auth.security.CustomUserDetails;
import com.devqoo.backend.user.enums.UserRoleType;
import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

class JwtAuthenticationFilterTest {

    private JwtProvider jwtProvider;
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @BeforeEach
    void setUp() {

        String accessKey = "mock-access-secret-key-must-be-32bytes!!";
        String refreshKey = "mock-refresh-secret-key-must-be-32byte!!";
        int accessExpireTime = 300000;
        int refreshExpireTime = 330000;

        jwtProvider = new JwtProvider(accessKey, refreshKey, accessExpireTime, refreshExpireTime);
        jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtProvider);

        SecurityContextHolder.clearContext();
    }

    @Test
    void doFilterInternal() throws Exception {

        // given
        Long userId = 1L;
        String email = "test@test.com";
        UserRoleType role = UserRoleType.STUDENT;

        String token =
            jwtProvider.generateAccessToken(userId, email, role);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + token);

        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = mock(FilterChain.class);

        // when
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // then
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        assertEquals(userId, userDetails.userId());
        assertEquals(email, userDetails.email());

        verify(filterChain).doFilter(request, response);
    }
}
