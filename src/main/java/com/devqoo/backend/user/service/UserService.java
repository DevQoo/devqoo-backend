package com.devqoo.backend.user.service;

import static com.devqoo.backend.common.exception.ErrorCode.EMAIL_ALREADY_EXISTS;
import static com.devqoo.backend.common.exception.ErrorCode.NICKNAME_ALREADY_EXISTS;
import static com.devqoo.backend.common.exception.ErrorCode.USER_NOT_FOUND;

import com.devqoo.backend.common.exception.BusinessException;
import com.devqoo.backend.user.dto.form.NicknameUpdateForm;
import com.devqoo.backend.user.dto.form.SignUpForm;
import com.devqoo.backend.user.dto.response.UserResponseDto;
import com.devqoo.backend.user.entity.User;
import com.devqoo.backend.user.enums.UserRoleType;
import com.devqoo.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    // 회원가입
    @Transactional
    public void signUp(SignUpForm signUpForm) {

        // 이메일, 닉네임 중복 확인
        validateEmail(signUpForm.email());
        validateNickname(signUpForm.nickName());

        // 회원 가입
        User user = User.builder()
            .email(signUpForm.email())
            .nickname(signUpForm.nickName())
            .password(passwordEncoder.encode(signUpForm.password()))
            .profileUrl(null) // S3 개발 후 수정
            .role(UserRoleType.STUDENT)
            .build();

        userRepository.save(user);
    }

    /*
     * 조회 (userId 기준)
     * 존재 하지 않으면 BusinessException 발생
     * */
    @Transactional(readOnly = true)
    public User findById(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new BusinessException(USER_NOT_FOUND));
    }

    // 이메일 중복 확인
    private void validateEmail(String email) {

        if (userRepository.existsByEmail(email)) {
            throw new BusinessException(EMAIL_ALREADY_EXISTS);
        }
    }

    // 닉네임 중복 확인
    private void validateNickname(String nickname) {

        if (userRepository.existsByNickname(nickname)) {
            throw new BusinessException(NICKNAME_ALREADY_EXISTS);
        }
    }

    // 닉네임 수정
    @Transactional
    public UserResponseDto updateUserNickname(Long userId, NicknameUpdateForm nicknameUpdateForm) {

        // 닉네임 중복 체크
        String nickname = nicknameUpdateForm.nickname();
        validateNickname(nickname);

        // userId 체크
        User user = this.findById(userId);
        user.updateNickname(nickname);

        return UserResponseDto.from(user);
    }
}
