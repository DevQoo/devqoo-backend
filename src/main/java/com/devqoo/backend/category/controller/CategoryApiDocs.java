package com.devqoo.backend.category.controller;

import com.devqoo.backend.category.dto.form.RegisterCategoryForm;
import com.devqoo.backend.category.dto.response.CategoryResponseDto;
import com.devqoo.backend.common.response.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;

@Tag(name = "Category", description = "카테고리 관련 API 입니다.")
public interface CategoryApiDocs {


    @Operation(summary = "카테고리 생성", description = "카테고리를 생성합니다.")
    ResponseEntity<CommonResponse<CategoryResponseDto>> createCategory(
        RegisterCategoryForm registerCategoryForm
    );

    @Operation(summary = "카테고리 목록 조회", description = "카테고리 목록을 조회합니다.")
    ResponseEntity<CommonResponse<List<CategoryResponseDto>>> getCategories();


    @Operation(summary = "카테고리 수정", description = "카테고리를 수정합니다.")
    ResponseEntity<CommonResponse<CategoryResponseDto>> updateCategory(
        Long categoryId,
        RegisterCategoryForm registerCategoryForm
    );

    @Operation(summary = "카테고리 삭제", description = "카테고리를 삭제합니다.")
    ResponseEntity<CommonResponse<Void>> deleteCategory(Long categoryId);

}
