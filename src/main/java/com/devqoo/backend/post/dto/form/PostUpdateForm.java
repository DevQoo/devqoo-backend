package com.devqoo.backend.post.dto.form;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostUpdateForm {

    private String title;
    private String content;
}
