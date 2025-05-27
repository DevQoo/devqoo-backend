package com.devqoo.backend.comment.dto.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegisterCommentForm(

    @NotNull(message = "작성자 ID는 필수입니다.")
    Long authorId,  // TODO : SecurityContextHolder에서 가져오기

    @NotNull(message = "게시글 ID는 필수입니다.")
    Long postId,

    @NotBlank(message = "댓글 내용은 필수입니다.")
    String content
) {

}
