package com.devqoo.backend.post.controller;

import com.devqoo.backend.common.response.CommonResponse;
import com.devqoo.backend.post.dto.form.PostRegisterForm;
import com.devqoo.backend.post.dto.response.PostResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Post", description = "게시글 관련 API 입니다.")
public interface PostApiDocs {

    // 게시글 생성
    @Operation(summary = "게시글 작성", description = "게시글을 생성합니다.")
    ResponseEntity<CommonResponse<Long>> createPost(@RequestBody @Valid PostRegisterForm postRegisterForm);

    // 게시글 상세 조회
    @Operation(summary = "게시글 상세 조회", description = "해당 게시글의 내용을 상세 조회합니다.")
    ResponseEntity<CommonResponse<PostResponseDto>> getPostDetail(@PathVariable Long postId);
}
