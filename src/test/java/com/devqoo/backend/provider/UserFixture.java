package com.devqoo.backend.provider;

import com.devqoo.backend.user.entity.User;
import com.devqoo.backend.user.enums.UserRoleType;
import org.springframework.test.util.ReflectionTestUtils;

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
        ReflectionTestUtils.setField(user, USER_ID_FIELD_NAME, userId);
        return user;
    }

}
