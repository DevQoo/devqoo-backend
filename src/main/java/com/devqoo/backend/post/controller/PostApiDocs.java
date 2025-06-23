package com.devqoo.backend.post.controller;

import com.devqoo.backend.common.response.CommonResponse;
import com.devqoo.backend.post.dto.form.PostForm;
import com.devqoo.backend.post.dto.response.CursorPageResponse;
import com.devqoo.backend.post.dto.response.PostResponseDto;
import com.devqoo.backend.post.enums.PostSortField;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Post", description = "게시글 관련 API 입니다.")
public interface PostApiDocs {

    // 게시글 생성
    @Operation(summary = "게시글 작성", description = "게시글을 생성합니다.")
    ResponseEntity<CommonResponse<Long>> createPost(@RequestBody @Valid PostForm postForm);

    // 게시글 수정
    @Operation(summary = "게시글 수정", description = "해당 게시글의 내용을 수정합니다.")
    ResponseEntity<CommonResponse<Long>> updatePost(@PathVariable Long postId, @RequestBody @Valid PostForm postForm);

    // 게시글 삭제
    @Operation(summary = "게시글 삭제", description = "해당 게시글을 삭제합니다.")
    ResponseEntity<CommonResponse<Void>> deletePost(@PathVariable Long postId);

    // 게시글 상세 조회
    @Operation(summary = "게시글 상세 조회", description = "해당 게시글의 내용을 상세 조회합니다.")
    ResponseEntity<CommonResponse<PostResponseDto>> getPostDetail(@PathVariable Long postId);

    // 게시글 조회 + 검색
    @Operation(summary = "게시글 커서 기반 조회", description = "검색 + 정렬 기준에 따라 커서 방식으로 게시글을 조회합니다.")
    ResponseEntity<CommonResponse<CursorPageResponse<PostResponseDto>>> getPostsByCursor(
        @RequestParam(required = false) String keyword,
        @RequestParam(required = false, defaultValue = "title_content") String searchType,
        @RequestParam(defaultValue = "POST_ID") PostSortField sortField,
        @RequestParam(required = false) Long lastPostId,
        @RequestParam(defaultValue = "10") int size
    );

    // 게시글 조회수 증가
    @Operation(summary = "게시글 조회수 증가", description = "해당 게시글의 조회수를 증가시킵니다.")
    ResponseEntity<CommonResponse<Void>> increaseViewCount(@PathVariable Long postId);
}
