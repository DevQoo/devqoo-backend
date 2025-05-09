package com.devqoo.backend.category.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.devqoo.backend.category.dto.form.RegisterCategoryForm;
import com.devqoo.backend.category.entity.Category;
import com.devqoo.backend.category.repository.CategoryRepository;
import com.devqoo.backend.common.exception.BusinessException;
import com.devqoo.backend.provider.EntityProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    public static final String CATEGORY_NAME = "질문 게시판";

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @DisplayName("create: 카테고리 생성 테스트")
    @Test
    void shouldCreateCategory_whenCategoryNameDoesNotExist() {
        // given
        RegisterCategoryForm form = new RegisterCategoryForm(CATEGORY_NAME);
        Category category = EntityProvider.createCategory(CATEGORY_NAME);
        given(categoryRepository.existsByCategoryName(CATEGORY_NAME)).willReturn(false);
        given(categoryRepository.save(any(Category.class))).willReturn(category);

        // when
        Category createdCategory = categoryService.create(form);

        // then
        assertThat(createdCategory.getCategoryName()).isEqualTo(CATEGORY_NAME);
    }

    @DisplayName("create: 이미 존재하는 카테고리 이름이면 예외를 던진다")
    @Test
    void shouldThrowException_whenCategoryNameAlreadyExists() {
        // given
        RegisterCategoryForm form = new RegisterCategoryForm(CATEGORY_NAME);
        Category category = EntityProvider.createCategory(CATEGORY_NAME);
        given(categoryRepository.existsByCategoryName(CATEGORY_NAME)).willReturn(true);

        // when & then
        assertThatThrownBy(() -> categoryService.create(form))
            .isInstanceOf(BusinessException.class);
    }
}
