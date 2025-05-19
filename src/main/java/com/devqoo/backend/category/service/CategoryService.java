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
        if (categoryRepository.existsByCategoryName(categoryName)) {
            throw new BusinessException(ErrorCode.CATEGORY_NAME_DUPLICATED);
        }
        Category category = new Category(categoryName);
        return categoryRepository.save(category);
    }

    @Transactional(readOnly = true)
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Transactional
    public Category update(Long categoryId, RegisterCategoryForm registerCategoryForm) {
        Category category = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new BusinessException(ErrorCode.CATEGORY_NOT_FOUND));
        String categoryName = registerCategoryForm.categoryName();
        if (categoryRepository.existsByCategoryName(categoryName)) {
            throw new BusinessException(ErrorCode.CATEGORY_NAME_DUPLICATED);
        }
        category.update(categoryName);
        return category;
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

    @Transactional
    public void deleteById(Long categoryId) {
        // TODO 소프트 삭제, 하드 삭제 구현 여부 상의하기
//        categoryRepository.deleteById(categoryId);
    }
}
