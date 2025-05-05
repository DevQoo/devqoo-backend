package com.devqoo.backend.post.controller;

import com.devqoo.backend.common.response.CommonResponse;
import com.devqoo.backend.post.dto.form.PostRegisterForm;
import com.devqoo.backend.post.service.PostFacade;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController implements PostApiDocs {

    private final PostFacade postFacade;


    /* post create */
    @Override
    @PostMapping
    public ResponseEntity<CommonResponse<Long>> createPost(
        @RequestBody @Valid PostRegisterForm postRegisterForm
    ) {
        Long postId = postFacade.createPost(postRegisterForm);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(CommonResponse.success(HttpStatus.CREATED.value(), postId));
    }
}
