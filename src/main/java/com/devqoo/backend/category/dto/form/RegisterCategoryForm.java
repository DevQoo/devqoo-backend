package com.devqoo.backend.category.dto.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterCategoryForm(
    @NotBlank(message = "카테고리 이름은 필수입니다.")
    @Size(min = 2, max = 20, message = "카테고리 이름은 2자 이상 20자 이하로 입력해주세요.")
    String categoryName
) {

}
