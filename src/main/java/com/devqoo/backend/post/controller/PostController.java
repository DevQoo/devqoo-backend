package com.devqoo.backend.post.controller;

import com.devqoo.backend.common.response.CommonResponse;
import com.devqoo.backend.post.dto.response.PostResponseDto;
import com.devqoo.backend.post.dto.form.PostRegisterForm;
import com.devqoo.backend.post.service.PostFacade;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController implements PostApiDocs {

    private final PostFacade postFacade;


    // 게시글 생성
    @Override
    @PostMapping
    public ResponseEntity<CommonResponse<Long>> createPost(@RequestBody @Valid PostRegisterForm postRegisterForm) {

        Long postId = postFacade.createPost(postRegisterForm);

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(CommonResponse.success(HttpStatus.CREATED.value(), postId));
    }

    // 게시글 상세 조회
    @Override
    @GetMapping("/{postId}")
    public ResponseEntity<CommonResponse<PostResponseDto>> getPostDetail(@PathVariable Long postId) {

        PostResponseDto postDto = postFacade.getPostDetail(postId);

        return ResponseEntity.status(HttpStatus.OK)
            .body(CommonResponse.success(HttpStatus.OK.value(), postDto));
    }
}
