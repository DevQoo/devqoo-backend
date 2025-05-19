package com.devqoo.backend.category.service;

import static com.devqoo.backend.common.exception.ErrorCode.CATEGORY_NOT_FOUND;

import com.devqoo.backend.category.dto.form.RegisterCategoryForm;
import com.devqoo.backend.category.entity.Category;
import com.devqoo.backend.category.repository.CategoryRepository;
import com.devqoo.backend.common.exception.BusinessException;
import com.devqoo.backend.common.exception.ErrorCode;
import java.util.List;
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
        validateCategoryName(categoryName);
        Category category = new Category(categoryName);
        return categoryRepository.save(category);
    }

    @Transactional(readOnly = true)
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Transactional
    public Category update(Long categoryId, RegisterCategoryForm registerCategoryForm) {
        Category category = findById(categoryId);
        String categoryName = registerCategoryForm.categoryName();
        validateCategoryName(categoryName);
        category.update(categoryName);
        return category;
    }

    private void validateCategoryName(String categoryName) {
        if (categoryRepository.existsByCategoryName(categoryName)) {
            throw new BusinessException(ErrorCode.CATEGORY_NAME_DUPLICATED);
        }
    }

    /*
     * 조회 (categoryId 기준)
     * 존재 하지 않으면 BusinessException 발생
     * */
    @Transactional(readOnly = true)
    public Category findById(Long categoryId) {
        return categoryRepository.findById(categoryId)
            .orElseThrow(() -> new BusinessException(CATEGORY_NOT_FOUND));
    }
}
