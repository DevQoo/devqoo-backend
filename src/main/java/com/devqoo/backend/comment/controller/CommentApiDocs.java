package com.devqoo.backend.comment.controller;

import com.devqoo.backend.comment.dto.form.RegisterCommentForm;
import com.devqoo.backend.comment.dto.response.CommentResponseDto;
import com.devqoo.backend.common.response.CommonResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Comment", description = "댓글 관련 API 입니다.")
public interface CommentApiDocs {

    // 댓글 목록 조회
    ResponseEntity<CommonResponse<CommentResponseDto>> getComments();

    // 댓글 생성
    ResponseEntity<CommonResponse<Long>> createComment(RegisterCommentForm form);

    // 댓글 수정
    ResponseEntity<CommonResponse<Long>> updateComment(Long commentId, RegisterCommentForm form);

    // 댓글 삭제
    ResponseEntity<Void> deleteComment(Long commentId);

}
