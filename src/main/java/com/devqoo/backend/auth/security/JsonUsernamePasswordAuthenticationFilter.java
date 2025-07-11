package com.devqoo.backend.auth.security;

import com.devqoo.backend.auth.dto.form.SignInForm;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@RequiredArgsConstructor
public class JsonUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final ObjectMapper objectMapper;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
        throws AuthenticationException {

        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }

        try {
            SignInForm signInForm = objectMapper.readValue(request.getInputStream(), SignInForm.class);

            UsernamePasswordAuthenticationToken authRequest =
                new UsernamePasswordAuthenticationToken(signInForm.email(), signInForm.password());

            setDetails(request, authRequest);
            return this.getAuthenticationManager().authenticate(authRequest);
        } catch (IOException exception) {
            throw new AuthenticationServiceException("Request parsing failed", exception);
        }
    }

    public static class Configurer extends
        AbstractAuthenticationFilterConfigurer<HttpSecurity, Configurer, JsonUsernamePasswordAuthenticationFilter> {

        private final ObjectMapper objectMapper;

        public Configurer(ObjectMapper objectMapper) {
            super(new JsonUsernamePasswordAuthenticationFilter(objectMapper), SecurityMatchers.LOGIN_URL);
            this.objectMapper = objectMapper;
        }

        @Override
        protected RequestMatcher createLoginProcessingUrlMatcher(String loginProcessingUrl) {
            return new AntPathRequestMatcher(loginProcessingUrl, HttpMethod.POST.name());
        }

        @Override
        public void init(HttpSecurity http) throws Exception {
            loginProcessingUrl(SecurityMatchers.LOGIN_URL);
        }
    }
}
