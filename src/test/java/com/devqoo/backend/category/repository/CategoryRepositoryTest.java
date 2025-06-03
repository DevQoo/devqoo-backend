package com.devqoo.backend.category.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.devqoo.backend.category.entity.Category;
import com.devqoo.backend.common.config.JpaAuditingConfiguration;
import com.devqoo.backend.common.config.QuerydslConfig;
import com.devqoo.backend.provider.EntityProvider;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@Import({JpaAuditingConfiguration.class, QuerydslConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class CategoryRepositoryTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private CategoryRepository categoryRepository;

    @DisplayName("existsByCategoryName : 카테고리 이름이 이미 존재하는지 확인")
    @Test
    void shouldReturnTrue_whenCategoryNameAlreadyExists() {
        // given
        String categoryName = "질문 게시판";
        Category category = EntityProvider.createCategory(categoryName);
        categoryRepository.save(category);
        entityManager.flush();
        entityManager.clear();
        // when
        boolean isExists = categoryRepository.existsByCategoryName(categoryName);
        // then
        assertThat(isExists).isTrue();
    }

    @DisplayName("existsByCategoryName : 카테고리 이름이 존재하지 않는지 확인")
    @Test
    void shouldReturnFalse_whenCategoryNameDoesNotExist() {
        // given
        String categoryName = "질문 게시판";
        // when
        boolean isExists = categoryRepository.existsByCategoryName(categoryName);
        // then
        assertThat(isExists).isFalse();
    }

}
