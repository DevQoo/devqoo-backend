package com.devqoo.backend.category.dto.form;

import jakarta.validation.constraints.NotBlank;

public record RegisterCategoryForm(
    @NotBlank(message = "카테고리 이름은 필수입니다.")
    String categoryName
) {

}
