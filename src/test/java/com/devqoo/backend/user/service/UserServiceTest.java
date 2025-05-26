//package com.devqoo.backend.user.service;
//
//import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNull;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.Mockito.verify;
//
//import com.devqoo.backend.common.exception.BusinessException;
//import com.devqoo.backend.common.exception.ErrorCode;
//import com.devqoo.backend.user.dto.form.SignUpForm;
//import com.devqoo.backend.user.entity.User;
//import com.devqoo.backend.user.enums.UserRoleType;
//import com.devqoo.backend.user.repository.UserRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.ArgumentCaptor;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//@ExtendWith(MockitoExtension.class)
//class UserServiceTest {
//
//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private PasswordEncoder passwordEncoder;
//
//    @InjectMocks
//    private UserService userService;
//
//    private SignUpForm signUpForm;
//
//
//    @BeforeEach
//    void setUp() {
//        signUpForm = SignUpForm.builder()
//            .email("test@example.com")
//            .nickName("tester")
//            .password("password123")
//            .passwordConfirm("different123")
//            .build();
//    }
//
//    @DisplayName("회원가입")
//    @Test
//    void signUp() {
//        // given
//        String encodedPassword = "encodedPassword123";
//        Mockito.when(passwordEncoder.encode(signUpForm.getPassword())).thenReturn(encodedPassword);
//
//        // when
//        userService.signUp(signUpForm);
//
//        // then
//        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
//        verify(userRepository).save(userCaptor.capture());
//
//        User user = userCaptor.getValue();
//        assertEquals(signUpForm.getEmail(), user.getEmail());
//        assertEquals(signUpForm.getNickName(), user.getNickname());
//        assertEquals(encodedPassword, user.getPassword());
//        assertEquals(UserRoleType.STUDENT, user.getRole());
//        assertNull(user.getProfileUrl());
//    }
//
//
//    @DisplayName("이메일 중복일 경우")
//    @Test
//    void validateEmail_already() {
//        // given
//        Mockito.when(userRepository.existsByEmail(signUpForm.getEmail())).thenReturn(true);
//
//        // when && then
//        BusinessException exception = assertThrows(BusinessException.class, () -> {
//            userService.validateEmail(signUpForm.getEmail());
//        });
//
//        assertEquals(ErrorCode.EMAIL_ALREADY_EXISTS, exception.getErrorCode());
//    }
//
//    @DisplayName("이메일 중복 아닐 경우")
//    @Test
//    void validateEmail_notAlready() {
//        // given
//        Mockito.when(userRepository.existsByEmail(signUpForm.getEmail())).thenReturn(false);
//
//        // when && then
//        assertDoesNotThrow(() -> {
//            userService.validateEmail(signUpForm.getEmail());
//        });
//    }
//
//
//    @DisplayName("닉네임 중복일 경우")
//    @Test
//    void validateNickname_already() {
//        // given
//        Mockito.when(userRepository.existsByNickname(signUpForm.getNickName())).thenReturn(true);
//
//        // when && then
//        BusinessException exception = assertThrows(BusinessException.class, () -> {
//            userService.validateNickname(signUpForm.getNickName());
//        });
//
//        assertEquals(ErrorCode.NICKNAME_ALREADY_EXISTS, exception.getErrorCode());
//    }
//
//    @DisplayName("닉네임 중복 아닐 경우")
//    @Test
//    void validateNickname_notAlready() {
//        // given
//        Mockito.when(userRepository.existsByNickname(signUpForm.getNickName())).thenReturn(false);
//
//        // when && then
//        assertDoesNotThrow(() -> {
//            userService.validateNickname(signUpForm.getNickName());
//        });
//    }
//}
