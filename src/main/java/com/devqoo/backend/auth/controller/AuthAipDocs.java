package com.devqoo.backend.auth.controller;

import com.devqoo.backend.auth.dto.form.SignInForm;
import com.devqoo.backend.common.response.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface AuthAipDocs {

    // 로그인
    @Operation(summary = "로그인", description = "로그인을 진행합니다.")
    ResponseEntity<CommonResponse<String>> signIn(@RequestBody @Valid SignInForm signInForm);
}
