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
import java.util.Optional;
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
        given(categoryRepository.existsByCategoryName(CATEGORY_NAME)).willReturn(true);

        // when & then
        assertThatThrownBy(() -> categoryService.create(form))
            .isInstanceOf(BusinessException.class);
    }

    @DisplayName("update: 카테고리 수정 테스트")
    @Test
//    @Disabled("수정 테스트는 아직 구현되지 않음")
    void shouldUpdateCategory_whenCategoryExists() {
        // given
        Category category = EntityProvider.createCategory(CATEGORY_NAME);
        given(categoryRepository.findById(any())).willReturn(Optional.of(category));

        String newCategoryName = "New Category Name";
        RegisterCategoryForm form = new RegisterCategoryForm(newCategoryName);
        given(categoryRepository.existsByCategoryName(form.categoryName())).willReturn(false);

        // when
        categoryService.update(1L, form);

        // then
        assertThat(category.getCategoryName()).isEqualTo(newCategoryName);
    }

    // 수정 테스트 실패 테스트 not found, duplicate name
    @DisplayName("update: 카테고리 수정 시 카테고리 이름이 중복되면 예외를 던진다")
    @Test
    void shouldThrowException_whenCategoryNameAlreadyExistsOnUpdate() {
        // given
        Category category = EntityProvider.createCategory(CATEGORY_NAME);
        given(categoryRepository.findById(any())).willReturn(Optional.of(category));

        String newCategoryName = "New Category Name";
        RegisterCategoryForm form = new RegisterCategoryForm(newCategoryName);
        given(categoryRepository.existsByCategoryName(form.categoryName())).willReturn(true);

        // when & then
        assertThatThrownBy(() -> categoryService.update(1L, form))
            .isInstanceOf(BusinessException.class);
    }

    @DisplayName("update: 카테고리 수정 시 카테고리가 존재하지 않으면 예외를 던진다")
    @Test
    void shouldThrowException_whenCategoryDoesNotExistOnUpdate() {
        // given
        RegisterCategoryForm form = new RegisterCategoryForm(CATEGORY_NAME);
        given(categoryRepository.findById(any())).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> categoryService.update(1L, form))
            .isInstanceOf(BusinessException.class);
    }
}
