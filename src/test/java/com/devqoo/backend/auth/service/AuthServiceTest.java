package com.devqoo.backend.auth.service;

import com.devqoo.backend.auth.dto.form.SignInForm;
import com.devqoo.backend.auth.dto.response.TokenResponseDto;
import com.devqoo.backend.auth.jwt.JwtProvider;
import com.devqoo.backend.auth.repository.AuthRepository;
import com.devqoo.backend.common.exception.BusinessException;
import com.devqoo.backend.user.entity.User;
import com.devqoo.backend.user.enums.UserRoleType;
import com.devqoo.backend.user.repository.UserRepository;
import java.lang.reflect.Field;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthRepository authRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtProvider jwtProvider;

    @InjectMocks
    private AuthService authService;

    private Long userId;
    private String email;
    private String password;
    private String encodedPassword;
    private String accessToken;
    private String refreshToken;
    private int refreshExpireTime;
    private User mockUser;

    @BeforeEach
    void setUp() throws Exception {

        userId = 1L;
        email = "tot0119@naver.com";
        password = "password";
        encodedPassword = "encodedPassword";
        accessToken = "access.token";
        refreshToken = "refresh.token";
        refreshExpireTime = 3600;

        Field field = AuthService.class.getDeclaredField("refreshExpireTime");
        field.setAccessible(true);
        field.set(authService, refreshExpireTime);

        mockUser = User.builder()
            .email(email)
            .password(encodedPassword)
            .role(UserRoleType.STUDENT)
            .build();

        Field userIdField = User.class.getDeclaredField("userId");
        userIdField.setAccessible(true);
        userIdField.set(mockUser, userId);
    }

    @Test
    void signIn_success() {

        // Given
        SignInForm form = new SignInForm(email, password);

        Mockito.when(userRepository.findByEmail(email)).thenReturn(Optional.of(mockUser));
        Mockito.when(passwordEncoder.matches(password, encodedPassword)).thenReturn(true);
        Mockito.when(jwtProvider.generateRefreshToken(
            userId, email, UserRoleType.STUDENT)).thenReturn(refreshToken);
        Mockito.when(jwtProvider.generateAccessToken(
            userId, email, UserRoleType.STUDENT)).thenReturn(accessToken);

        // When
        TokenResponseDto result = authService.signIn(form);

        // Then
        Assertions.assertEquals(accessToken, result.accessToken());
        Assertions.assertEquals(refreshToken, result.refreshToken());
        Assertions.assertEquals(refreshExpireTime, result.refreshExpireTime());

        Mockito.verify(authRepository).saveRefreshToken(userId, refreshToken, refreshExpireTime);
    }


    @Test
    void signIn_fail_invalidCredentials() {

        // Given
        SignInForm form = new SignInForm(email, password);

        Mockito.when(userRepository.findByEmail(email)).thenReturn(Optional.of(mockUser));
        Mockito.when(passwordEncoder.matches(password, encodedPassword)).thenReturn(false);

        // Expect
        Assertions.assertThrows(BusinessException.class, () -> authService.signIn(form));
    }
}
