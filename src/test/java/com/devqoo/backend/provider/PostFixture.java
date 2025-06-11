package com.devqoo.backend.provider;

import com.devqoo.backend.category.entity.Category;
import com.devqoo.backend.post.entity.Post;
import com.devqoo.backend.user.entity.User;
import java.lang.reflect.Field;
import org.springframework.test.util.ReflectionTestUtils;

public abstract class PostFixture {

    public static Post createPost() {
        return createPost(1L);
    }

    public static Post createPost(Long postId) {
        return createPost(
            postId,
            UserFixture.createUser(),
            EntityProvider.createCategory("카테고리"),
            "제목",
            "내용"
        );
    }

    public static Post createPost(Long postId, User user, Category category, String title, String content) {
        Post post = Post.builder()
            .title(title)
            .content(content)
            .category(category)
            .user(user)
            .build();
        ReflectionTestUtils.setField(post, "postId", postId);
        return post;
    }

    public static <T> T setId(T entity, Long id, String fieldName) {
        try {
            Field field = entity.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(entity, id);
        } catch (Exception e) {
            throw new RuntimeException("ID 주입 실패", e);
        }
        return entity;
    }
}
