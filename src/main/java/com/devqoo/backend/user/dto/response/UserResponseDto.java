package com.devqoo.backend.user.dto.response;

import com.devqoo.backend.user.entity.User;
import com.devqoo.backend.user.enums.UserRoleType;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public record UserResponseDto(
    Long userId,
    String nickname,
    String email,
    String profileUrl,
    UserRoleType role,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    LocalDateTime createdAt
) {

    public static UserResponseDto from(User user) {
        return new UserResponseDto(
            user.getUserId(),
            user.getNickname(),
            user.getEmail(),
            user.getProfileUrl(),
            user.getRole(),
            user.getCreatedAt()
        );
    }
}
