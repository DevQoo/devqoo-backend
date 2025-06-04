package com.devqoo.backend.post.enums;

import static com.devqoo.backend.common.exception.ErrorCode.INVALID_ENUM_VALUE;

import com.devqoo.backend.common.exception.BusinessException;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class PostSortFieldConverter implements Converter<String, PostSortField> {

    @Override
    public PostSortField convert(String source) {
        return switch (source.toLowerCase()) {
            case "postid" -> PostSortField.POST_ID;
            case "viewcount" -> PostSortField.VIEW_COUNT;
            default -> throw new BusinessException(INVALID_ENUM_VALUE);
        };
    }
}
