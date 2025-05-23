package com.devqoo.backend.comment.controller;

import com.devqoo.backend.comment.dto.form.RegisterCommentForm;
import com.devqoo.backend.comment.dto.response.CommentResponseDto;
import com.devqoo.backend.common.response.CommonResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Comment", description = "댓글 관련 API 입니다.")
public interface CommentApiDocs {

    ResponseEntity<CommonResponse<CommentResponseDto>> getComments();

    ResponseEntity<CommonResponse<Long>> createComment(RegisterCommentForm form);

    ResponseEntity<CommonResponse<Long>> updateComment(Long commentId, RegisterCommentForm form);

    ResponseEntity<Void> deleteComment(Long commentId);

}
