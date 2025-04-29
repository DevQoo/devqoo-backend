package com.devqoo.backend.category.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.devqoo.backend.category.entity.Category;
import com.devqoo.backend.common.config.JpaAuditingConfiguration;
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
@Import({JpaAuditingConfiguration.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class CategoryRepositoryTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private CategoryRepository categoryRepository;

    @DisplayName("카테고리 저장 조회 테스트")
    @Test
    void given_categoryName_save_when_find_then_return_saved_category() {
        // given
        Category savedCategory = categoryRepository.save(new Category("질문 게시판"));
        entityManager.flush();
        // when
        Category category = categoryRepository.findById(savedCategory.getId()).orElse(null);
        // then
        assertThat(category).isNotNull();
    }
}
