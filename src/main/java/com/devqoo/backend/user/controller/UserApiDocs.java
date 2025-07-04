package com.devqoo.backend.user.controller;

import com.devqoo.backend.common.response.CommonResponse;
import com.devqoo.backend.user.dto.form.NicknameUpdateForm;
import com.devqoo.backend.user.dto.form.SignUpForm;
import com.devqoo.backend.user.dto.response.UserResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "User", description = "사용자 관련 API 입니다.")
public interface UserApiDocs {

    // 회원가입
    @Operation(summary = "회원가입", description = "회원가입을 진행합니다.")
    ResponseEntity<CommonResponse<Void>> signUp(@RequestBody @Valid SignUpForm signUpForm);

    // 닉네임 변경
    @Operation(summary = "닉네임 변경", description = "닉네임을 변경합니다.")
    ResponseEntity<CommonResponse<UserResponseDto>> updateUserNickname(
        @PathVariable Long userId,
        @RequestBody @Valid NicknameUpdateForm nicknameUpdateForm);
}
