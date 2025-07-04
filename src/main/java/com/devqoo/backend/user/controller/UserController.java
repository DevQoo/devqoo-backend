package com.devqoo.backend.user.controller;

import com.devqoo.backend.common.response.CommonResponse;
import com.devqoo.backend.user.dto.form.NicknameUpdateForm;
import com.devqoo.backend.user.dto.form.SignUpForm;
import com.devqoo.backend.user.dto.response.UserResponseDto;
import com.devqoo.backend.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController implements UserApiDocs {

    private final UserService userService;


    // 회원가입
    @Override
    @PostMapping
    public ResponseEntity<CommonResponse<Void>> signUp(@RequestBody @Valid SignUpForm signUpForm) {

        userService.signUp(signUpForm);

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(CommonResponse.success(HttpStatus.CREATED.value(), null));
    }

    // 닉네임 변경
    @Override
    @PatchMapping("/{userId}/nickname")
    public ResponseEntity<CommonResponse<UserResponseDto>> updateUserNickname(
        @PathVariable Long userId, @RequestBody @Valid NicknameUpdateForm nicknameUpdateForm) {

        UserResponseDto userResponseDto = userService.updateUserNickname(userId, nicknameUpdateForm);

        return ResponseEntity.status(HttpStatus.OK)
            .body(CommonResponse.success(HttpStatus.OK.value(), userResponseDto));
    }
}
