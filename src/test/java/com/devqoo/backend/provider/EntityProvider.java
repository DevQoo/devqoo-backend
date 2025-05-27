package com.devqoo.backend.provider;

import com.devqoo.backend.category.entity.Category;
import com.devqoo.backend.post.entity.Post;
import com.devqoo.backend.user.entity.User;
import com.devqoo.backend.user.enums.UserRoleType;

public abstract class EntityProvider {

    public static Category createCategory(String name) {
        return new Category(name);
    }

    public static User createUser() {
        return User.builder()
            .email("email")
            .password("password")
            .nickname("nickname")
            .profileUrl("profileUrl")
            .role(UserRoleType.STUDENT)
            .build();
    }

    public static Post createPost(User user, Category category) {
        return Post.builder()
            .title("제목")
            .content("내용")
            .category(category)
            .user(user)
            .build();
    }
}
