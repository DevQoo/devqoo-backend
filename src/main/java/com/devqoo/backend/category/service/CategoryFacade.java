package com.devqoo.backend.category.service;

import com.devqoo.backend.category.dto.form.RegisterCategoryForm;
import com.devqoo.backend.category.dto.response.CategoryResponseDto;
import com.devqoo.backend.category.entity.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryFacade {

    private final CategoryService categoryService;

    public CategoryResponseDto createCategory(RegisterCategoryForm form) {
        Category category = categoryService.create(form);
        return new CategoryResponseDto(category);
    }
}
