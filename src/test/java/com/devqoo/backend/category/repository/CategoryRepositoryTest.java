package com.devqoo.backend.category.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.devqoo.backend.category.entity.Category;
import com.devqoo.backend.common.config.JpaAuditingConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@Import(JpaAuditingConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @DisplayName("카테고리를 저장하면, 저장된 값을 ID로 조회할 수 있다.")
    @Test
    void givenCategoryName_whenSaving_thenCanRetrieveById() {
        // given
        String categoryName = "질문 게시판";
        Category category = Category.builder()
            .categoryName(categoryName)
            .build();

        // when
        Category saved = categoryRepository.save(category);
        Category found = categoryRepository.findById(saved.getCategoryId()).orElse(null);

        // then
        assertThat(found).isNotNull();
        assertThat(found.getCategoryId()).isEqualTo(saved.getCategoryId());
        assertThat(found.getCategoryName()).isEqualTo(categoryName);
    }
}
