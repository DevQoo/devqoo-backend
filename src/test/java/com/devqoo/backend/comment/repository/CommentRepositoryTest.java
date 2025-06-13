package com.devqoo.backend.comment.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;

import com.devqoo.backend.category.entity.Category;
import com.devqoo.backend.comment.entity.Comment;
import com.devqoo.backend.common.config.JpaAuditingConfiguration;
import com.devqoo.backend.common.config.QuerydslConfig;
import com.devqoo.backend.post.entity.Post;
import com.devqoo.backend.user.entity.User;
import com.devqoo.backend.user.enums.UserRoleType;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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

    Post post;
    User user;

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

    @Nested
    @DisplayName("findCommentsByCursor : 커서 기반 페이징으로 댓글 조회")
    class FindCommentsByCursor {

        @DisplayName("댓글 조회 페이징")
        @Test
        void find_by_cursor_is_null() {

            System.out.println("post.getPostId() = " + post.getPostId());
            int size = 10;
            List<Comment> commentList = commentRepository.findCommentsByCursor(post.getPostId(), null, size);

            List<Long> idList = commentList.stream()
                .map(Comment::getCommentId)
                .toList();

            then(commentList)
                .hasSize(size + 1);
            then(idList)
                .isSorted();
        }

        @DisplayName("커서가 있는 경우 댓글 조회")
        @Test
        void find_by_cursor_id_not_null() {
            int size = 10;
            List<Comment> result = commentRepository.findCommentsByCursor(post.getPostId(), 3L, size);

            then(result)
                .hasSize(size + 1)
                .extracting(Comment::getCommentId)
                .allMatch(id -> id > 3L);
        }

    }

    private void setData() {
        Category category = new Category("질문 게시판");
        entityManager.persist(category);

        user = User.builder()
            .nickname(NICKNAME)
            .email(EMAIL)
            .password(PASSWORD)
            .profileUrl(null)
            .role(UserRoleType.STUDENT)
            .build();
        entityManager.persist(user);

        post = Post.builder()
            .user(user)
            .category(category)
            .title(TITLE)
            .content("테스트 내용")
            .build();
        entityManager.persist(post);

        for (int i = 0; i < 20; i++) {
            Comment comment = Comment.builder()
                .post(post)
                .author(user)
                .content(CONTENT + " " + i)
                .build();
            entityManager.persist(comment);
        }

        entityManager.flush();
        entityManager.clear();
    }
}
