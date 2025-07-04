package com.devqoo.backend.auth.security;

import io.swagger.v3.oas.models.PathItem.HttpMethod;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

public abstract class SecurityMatchers {

    public static final RequestMatcher NON_API =
        new NegatedRequestMatcher(new AntPathRequestMatcher("/api/**"));

    // 회원 가입
    public static final RequestMatcher SIGN_UP =
        new AntPathRequestMatcher("/api/users", HttpMethod.POST.name());

    // 로그인
    public static final RequestMatcher LOGIN =
        new AntPathRequestMatcher("/api/login", HttpMethod.POST.name());

    // 로그아웃
    public static final RequestMatcher LOGOUT =
        new AntPathRequestMatcher("/api/auth/logout", HttpMethod.POST.name());

    public static final String LOGIN_URL = "/api/auth/login";

    public static final RequestMatcher[] PERMIT_URI =
        new RequestMatcher[]{
            SIGN_UP, LOGIN, LOGOUT, NON_API
        };
}
