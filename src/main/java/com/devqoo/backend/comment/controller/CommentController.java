package com.devqoo.backend.comment.controller;

import com.devqoo.backend.comment.dto.form.RegisterCommentForm;
import com.devqoo.backend.comment.dto.response.CommentResponseDto;
import com.devqoo.backend.comment.service.CommentFacade;
import com.devqoo.backend.common.response.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController implements CommentApiDocs {

    private final CommentFacade commentFacade;

    @Override
    public ResponseEntity<CommonResponse<CommentResponseDto>> getComments() {
        return null;
    }

    @Override
    public ResponseEntity<CommonResponse<Long>> createComment(RegisterCommentForm form) {
        return null;
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
