package com.devqoo.backend.category.dto.form;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record RegisterCategoryForm(
    @NotBlank(message = "카테고리 이름은 필수입니다.")
    @Min(value = 2, message = "카테고리 이름은 최소 2자 이상이어야 합니다.")
    String categoryName
) {

}
