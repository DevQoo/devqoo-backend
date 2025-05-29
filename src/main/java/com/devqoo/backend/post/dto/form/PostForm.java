package com.devqoo.backend.post.dto.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PostForm(

    @NotNull(message = "카테고리 ID는 필수입니다.")
    Long categoryId,

    @NotNull(message = "작성자 ID는 필수입니다.")
    Long userId,

    @NotBlank(message = "제목은 필수입니다.")
    String title,

    @NotBlank(message = "내용은 필수입니다.")
    String content
) {

}
