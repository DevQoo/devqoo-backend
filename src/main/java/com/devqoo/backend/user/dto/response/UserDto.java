package com.devqoo.backend.user.dto.response;

import com.devqoo.backend.user.entity.User;
import com.devqoo.backend.user.enums.UserRoleType;

public record UserDto(
    Long userId,
    String nickname,
    String email,
    String profileUrl,
    UserRoleType role
) {

    public static UserDto from(User user) {
        return new UserDto(
            user.getUserId(),
            user.getNickname(),
            user.getEmail(),
            user.getProfileUrl(),
            user.getRole()
        );
    }
}
