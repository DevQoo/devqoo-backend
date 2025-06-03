package com.devqoo.backend.post.enums;

import static com.devqoo.backend.common.exception.ErrorCode.INVALID_ENUM_VALUE;

import com.devqoo.backend.common.exception.BusinessException;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class SortDirectionConverter implements Converter<String, SortDirection> {

    @Override
    public SortDirection convert(String source) {
        return switch (source.toLowerCase()) {
            case "asc" -> SortDirection.ASC;
            case "desc" -> SortDirection.DESC;
            default -> throw new BusinessException(INVALID_ENUM_VALUE);
        };
    }
}
