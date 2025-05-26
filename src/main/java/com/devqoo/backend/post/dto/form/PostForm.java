package com.devqoo.backend.post.dto.form;

import jakarta.validation.constraints.NotBlank;

public record PostForm(
    Long categoryId,

    Long userId,

    @NotBlank(message = "제목은 필수입니다.")
    String title,

    @NotBlank(message = "내용은 필수입니다.")
    String content
) {

}
