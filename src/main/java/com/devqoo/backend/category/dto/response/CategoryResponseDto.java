package com.devqoo.backend.category.dto.response;

import com.devqoo.backend.category.entity.Category;

public record CategoryResponseDto(
    Long categoryId,
    String categoryName
) {

    public CategoryResponseDto(Category category) {
        this(category.getCategoryId(), category.getCategoryName());
    }
}
