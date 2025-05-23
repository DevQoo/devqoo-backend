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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController implements CommentApiDocs {

    private final CommentService commentService;

    @Override
    public ResponseEntity<CommonResponse<CommentResponseDto>> getComments() {
        return null;
    }

    @Override
    public ResponseEntity<CommonResponse<Long>> createComment(@RequestBody @Valid RegisterCommentForm form) {
        Comment comment = commentService.createComment(form);
        int statusCode = HttpStatus.CREATED.value();
        CommonResponse<Long> response = CommonResponse.success(statusCode, comment.getCommentId());
        return ResponseEntity.status(statusCode).body(response);
    }

    @Override
    public ResponseEntity<CommonResponse<Long>> updateComment(Long commentId, RegisterCommentForm form) {
        return null;
    }

    @Override
    public ResponseEntity<Void> deleteComment(Long commentId) {
        return null;
    }

}
