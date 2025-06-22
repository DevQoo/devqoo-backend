package com.devqoo.backend.post.controller;

import com.devqoo.backend.common.response.CommonResponse;
import com.devqoo.backend.post.dto.form.PostForm;
import com.devqoo.backend.post.dto.response.CursorPageResponse;
import com.devqoo.backend.post.dto.response.PostResponseDto;
import com.devqoo.backend.post.enums.PostSortField;
import com.devqoo.backend.post.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController implements PostApiDocs {

    private final PostService postService;


    // 게시글 생성
    @PostMapping
    public ResponseEntity<CommonResponse<Long>> createPost(@RequestBody @Valid PostForm postForm) {

        Long postId = postService.createPost(postForm);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponse.success(HttpStatus.CREATED.value(), postId));
    }

    // 게시글 수정
    @PutMapping("/{postId}")
    public ResponseEntity<CommonResponse<Long>> updatePost(@PathVariable Long postId,
                                                           @RequestBody @Valid PostForm postForm) {

        Long updatePostId = postService.updatePost(postId, postForm);

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.success(HttpStatus.OK.value(), updatePostId));
    }

    // 게시글 삭제
    @DeleteMapping("/{postId}")
    public ResponseEntity<CommonResponse<Void>> deletePost(@PathVariable Long postId) {

        postService.deletePost(postId);

        return ResponseEntity.noContent().build();
    }

    // 게시글 상세 조회
    @GetMapping("/{postId}")
    public ResponseEntity<CommonResponse<PostResponseDto>> getPostDetail(@PathVariable Long postId) {

        PostResponseDto postDto = postService.getPostDetail(postId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.success(HttpStatus.OK.value(), postDto));
    }

    // 게시글 조회 + 검색
    @GetMapping
    public ResponseEntity<CommonResponse<CursorPageResponse<PostResponseDto>>> getPostsByCursor(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false, defaultValue = "title") String searchType,
            @RequestParam(defaultValue = "POST_ID") PostSortField sortField,
            @RequestParam(required = false) Long lastPostId,
            @RequestParam(defaultValue = "10") int size
    ) {

        CursorPageResponse<PostResponseDto> response = postService.getPostsByCursor(
                keyword, searchType, sortField, lastPostId, size);

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.success(HttpStatus.OK.value(), response));
    }

    // 게시글 조회수 증가
    @PostMapping("/{postId}/views")
    public ResponseEntity<CommonResponse<Void>> increaseViewCount(@PathVariable Long postId) {
        postService.increaseViewCount(postId);
        return ResponseEntity.ok(CommonResponse.success(HttpStatus.OK.value(), null));
    }
}
