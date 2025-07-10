package com.devqoo.backend.user.controller;

import com.devqoo.backend.auth.jwt.Auth;
import com.devqoo.backend.auth.security.CustomUserDetails;
import com.devqoo.backend.common.response.CommonResponse;
import com.devqoo.backend.post.dto.response.CursorPageResponse;
import com.devqoo.backend.post.dto.response.PostResponseDto;
import com.devqoo.backend.user.dto.form.NicknameUpdateForm;
import com.devqoo.backend.user.dto.form.PasswordUpdateForm;
import com.devqoo.backend.user.dto.form.SignUpForm;
import com.devqoo.backend.user.dto.response.UserResponseDto;
import com.devqoo.backend.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    // 비밀번호 변경
    @Override
    @PatchMapping("/{userId}/password")
    public ResponseEntity<CommonResponse<Void>> updateUserPassword(
        @PathVariable Long userId, @RequestBody @Valid PasswordUpdateForm passwordUpdateForm) {

        userService.updateUserPassword(userId, passwordUpdateForm);

        return ResponseEntity.status(HttpStatus.OK)
            .body(CommonResponse.success(HttpStatus.OK.value(), null));
    }

    // 내 게시글 목록
    @GetMapping("/comments")
    public ResponseEntity<CommonResponse<CursorPageResponse<PostResponseDto>>> getMyPosts(
        @Auth CustomUserDetails customUserDetails,
        @RequestParam(required = false) Long lastPostId,
        @RequestParam(defaultValue = "10") int size
    ) {

        CursorPageResponse<PostResponseDto> response =
            userService.getMyPosts(customUserDetails.userId(), lastPostId, size);

        return ResponseEntity.status(HttpStatus.OK)
            .body(CommonResponse.success(HttpStatus.OK.value(), response));
    }
}
