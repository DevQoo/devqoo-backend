package com.devqoo.backend.comment.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.devqoo.backend.category.entity.Category;
import com.devqoo.backend.comment.entity.Comment;
import com.devqoo.backend.common.config.JpaAuditingConfiguration;
import com.devqoo.backend.common.config.QuerydslConfig;
import com.devqoo.backend.post.entity.Post;
import com.devqoo.backend.user.entity.User;
import com.devqoo.backend.user.enums.UserRoleType;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
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
class CommentRepositoryTest {

    public static final String NICKNAME = "testUser";
    public static final String EMAIL = "test@mail.com";
    public static final String PASSWORD = "password";
    public static final String CONTENT = "테스트 댓글";
    public static final String TITLE = "테스트 포스트";
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    void setUp() {
        setData();
    }

    @DisplayName("findWithAuthorAndPostById : 댓글 ID로 댓글 fetch 조회")
    @Test
    void find_comment_by_id() {
        // given
        Long commentId = 1L;

        // when
        Comment comment = commentRepository.findWithAuthorAndPostById(commentId)
            .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));

        // then
        assertThat(comment.getCommentId()).isEqualTo(commentId);
        assertThat(comment.getAuthor().getNickname()).isEqualTo(NICKNAME);
        assertThat(comment.getPost().getTitle()).isEqualTo(TITLE);
    }

    private void setData() {
        Category category = new Category("질문 게시판");
        entityManager.persist(category);

        User user = User.builder()
            .nickname(NICKNAME)
            .email(EMAIL)
            .password(PASSWORD)
            .profileUrl(null)
            .role(UserRoleType.STUDENT)
            .build();
        entityManager.persist(user);

        Post post = Post.builder()
            .user(user)
            .category(category)
            .title(TITLE)
            .content("테스트 내용")
            .build();
        entityManager.persist(post);

        Comment comment = Comment.builder()
            .post(post)
            .author(user)
            .content(CONTENT)
            .build();
        entityManager.persist(comment);
        entityManager.flush();
        entityManager.clear();
    }
}
