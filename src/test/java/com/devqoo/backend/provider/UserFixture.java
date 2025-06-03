package com.devqoo.backend.provider;

import com.devqoo.backend.user.entity.User;
import com.devqoo.backend.user.enums.UserRoleType;
import java.lang.reflect.Field;

public abstract class UserFixture {

    public static final String USER_ID_FIELD_NAME = "userId";

    public static User createUser() {
        return createUser(1L, "email", "nickname", "profileUrl", "password");
    }

    public static User createUser(Long userId) {
        return createUser(userId, "email", "nickname", "profileUrl", "password");
    }

    public static User createUser(Long userId, String email, String nickname, String profileUrl, String password) {
        User user = User.builder()
            .email(email)
            .password(password)
            .nickname(nickname)
            .profileUrl(profileUrl)
            .role(UserRoleType.STUDENT)
            .build();
        return setId(user, userId, USER_ID_FIELD_NAME);
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
