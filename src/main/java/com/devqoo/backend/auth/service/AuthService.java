package com.devqoo.backend.auth.service;

import com.devqoo.backend.auth.dto.form.SignInForm;
import com.devqoo.backend.auth.dto.response.TokenResponseDto;
import com.devqoo.backend.auth.jwt.JwtProvider;
import com.devqoo.backend.auth.repository.AuthRepository;
import com.devqoo.backend.common.exception.BusinessException;
import com.devqoo.backend.common.exception.ErrorCode;
import com.devqoo.backend.user.entity.User;
import com.devqoo.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Value("${JWT_REFRESH_TIME}")
    private int refreshExpireTime;


    // 로그인
    @Transactional(readOnly = true)
    public TokenResponseDto signIn(SignInForm signInForm) {

        User user = userRepository.findByEmail(signInForm.email())
            .filter(sign -> passwordEncoder.matches(signInForm.password(), sign.getPassword()))
            .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_CREDENTIALS));

        String refreshToken =
            jwtProvider.generateRefreshToken(user.getUserId(), user.getEmail(), user.getRole());

        try {
            authRepository.saveRefreshToken(user.getUserId(), refreshToken, refreshExpireTime);
        } catch (RuntimeException ex) {
            throw new BusinessException(ErrorCode.REDIS_EXCEPTION);
        }

        String accessToken =
            jwtProvider.generateAccessToken(user.getUserId(), user.getEmail(), user.getRole());

        return new TokenResponseDto(accessToken, refreshToken, refreshExpireTime);
    }
}
