package com.devqoo.backend.post.controller;

import com.devqoo.backend.common.response.CommonResponse;
import com.devqoo.backend.post.dto.form.PostRegisterForm;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Post", description = "게시글 관련 API 입니다.")
public interface PostApiDocs {

    @Operation(summary = "게시글 작성", description = "게시글을 생성합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "게시글 생성 성공"),
        @ApiResponse(responseCode = "400", description = "유효성 검증 실패")
    })
    ResponseEntity<CommonResponse<Long>> createPost(
        @RequestBody @Valid PostRegisterForm postRegisterForm
    );
}
