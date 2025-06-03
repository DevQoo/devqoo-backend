package com.devqoo.backend.auth.controller;

import com.devqoo.backend.auth.dto.form.SignInForm;
import com.devqoo.backend.auth.dto.response.TokenResponseDto;
import com.devqoo.backend.auth.service.AuthService;
import com.devqoo.backend.common.response.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController implements AuthAipDocs {

    private final AuthService authService;


    // 로그인
    @PostMapping
    public ResponseEntity<CommonResponse<String>> signIn(SignInForm signInForm) {

        TokenResponseDto tokenResponse = authService.signIn(signInForm);

        ResponseCookie cookie =
            ResponseCookie.from("refreshToken", tokenResponse.refreshToken())
                .httpOnly(true)
                .secure(false) // HTTPS 진행시 true 로 변경
                .path("/")
                .maxAge(tokenResponse.refreshExpireTime() / 1000)
                .sameSite("Strict") // CSRF 공격 방지용
                .build();

        return ResponseEntity.status(HttpStatus.OK)
            .header(HttpHeaders.SET_COOKIE, cookie.toString())
            .body(CommonResponse.success(HttpStatus.OK.value(), tokenResponse.accessToken()));
    }
}
