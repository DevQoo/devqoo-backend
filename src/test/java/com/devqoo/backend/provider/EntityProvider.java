package com.devqoo.backend.provider;

import com.devqoo.backend.category.entity.Category;
import com.devqoo.backend.comment.entity.Comment;
import com.devqoo.backend.post.entity.Post;
import com.devqoo.backend.user.entity.User;
import java.lang.reflect.Field;

public abstract class EntityProvider {

    public static Category createCategory(String name) {
        Category category = new Category(name);
        return category;
    }


    public static Post createPost(User user, Category category) {
        Post post = Post.builder()
            .title("제목")
            .content("내용")
            .category(category)
            .user(user)
            .build();
        setId(post, 1L, "postId");
        return post;
    }

    public static Comment createComment(Post post, User user, String content) {
        Comment comment = Comment.builder()
            .post(post)
            .author(user)
            .content(content)
            .build();
        setId(comment, 1L, "commentId");
        return comment;
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
