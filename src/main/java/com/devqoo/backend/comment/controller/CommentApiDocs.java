package com.devqoo.backend.comment.controller;

import com.devqoo.backend.comment.dto.form.RegisterCommentForm;
import com.devqoo.backend.comment.dto.response.CommentResponseDto;
import com.devqoo.backend.common.response.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Comment", description = "댓글 관련 API 입니다.")
public interface CommentApiDocs {

    @Operation(summary = "댓글 목록 조회", description = "댓글 목록을 조회합니다.")
    ResponseEntity<CommonResponse<CommentResponseDto>> getComments();

    @Operation(summary = "댓글 생성", description = "댓글을 생성합니다.")
    ResponseEntity<CommonResponse<Long>> createComment(RegisterCommentForm form);

    @Operation(summary = "댓글 수정", description = "댓글을 수정합니다.")
    ResponseEntity<CommonResponse<Long>> updateComment(Long commentId, RegisterCommentForm form);

    @Operation(summary = "댓글 삭제", description = "댓글을 삭제합니다.")
    ResponseEntity<Void> deleteComment(Long commentId);

}
