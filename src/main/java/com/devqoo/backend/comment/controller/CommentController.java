package com.devqoo.backend.comment.controller;

import com.devqoo.backend.comment.dto.form.RegisterCommentForm;
import com.devqoo.backend.comment.dto.response.CommentResponseDto;
import com.devqoo.backend.comment.entity.Comment;
import com.devqoo.backend.comment.service.CommentService;
import com.devqoo.backend.common.response.CommonResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController implements CommentApiDocs {

    private final CommentService commentService;

    @Override
    @GetMapping
    public ResponseEntity<CommonResponse<CommentResponseDto>> getComments() {
        return null;
    }

    @Override
    @PostMapping
    public ResponseEntity<CommonResponse<Long>> createComment(@RequestBody @Valid RegisterCommentForm form) {
        Comment comment = commentService.createComment(form);
        int createdCode = HttpStatus.CREATED.value();
        CommonResponse<Long> response = CommonResponse.success(createdCode, comment.getCommentId());
        return ResponseEntity.status(createdCode).body(response);
    }

    @Override
    @PatchMapping("/{commentId}")
    public ResponseEntity<CommonResponse<Long>> updateComment(
        @PathVariable Long commentId,
        @Valid @RequestBody RegisterCommentForm form
    ) {
        commentService.updateComment(commentId, form);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Override
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        return null;
    }

}
