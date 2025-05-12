package com.devqoo.backend.category.service;

import com.devqoo.backend.category.dto.form.RegisterCategoryForm;
import com.devqoo.backend.category.entity.Category;
import com.devqoo.backend.category.repository.CategoryRepository;
import com.devqoo.backend.common.exception.BusinessException;
import com.devqoo.backend.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional
    public Category create(RegisterCategoryForm registerCategoryForm) {
        String categoryName = registerCategoryForm.categoryName();
        if (categoryRepository.existsByCategoryName(categoryName)) {
            throw new BusinessException(ErrorCode.CATEGORY_NAME_DUPLICATED);
        }
        Category category = new Category(categoryName);
        return categoryRepository.save(category);
    }
}
