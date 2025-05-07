package com.devqoo.backend.category.controller;

import com.devqoo.backend.category.dto.form.RegisterCategoryForm;
import com.devqoo.backend.category.dto.response.CategoryResponseDto;
import com.devqoo.backend.common.response.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;

@Tag(name = "Category", description = "카테고리 관련 API 입니다.")
public interface CategoryApiDocs {

    @RequestBody(
        description = "카테고리 생성 폼", required = true,
        content = @Content(schema = @Schema(implementation = RegisterCategoryForm.class))
    )
    @Operation(summary = "카테고리 생성", description = "카테고리를 생성합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "카테고리 생성 성공"),
        @ApiResponse(responseCode = "400", description = "유효성 검증 실패"),
        @ApiResponse(responseCode = "403", description = "권한 없음")
    })
    ResponseEntity<CommonResponse<CategoryResponseDto>> createCategory(
        RegisterCategoryForm registerCategoryForm
    );

    @Operation(summary = "카테고리 목록 조회", description = "카테고리 목록을 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "카테고리 목록 조회 성공")
    })
    ResponseEntity<CommonResponse<List<CategoryResponseDto>>> getCategories();

    @RequestBody(
        description = "카테고리 수정 폼", required = true,
        content = @Content(schema = @Schema(implementation = RegisterCategoryForm.class))
    )
    @Operation(summary = "카테고리 수정", description = "카테고리를 수정합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "카테고리 수정 성공"),
        @ApiResponse(responseCode = "400", description = "유효성 검증 실패"),
        @ApiResponse(responseCode = "404", description = "카테고리 없음"),
        @ApiResponse(responseCode = "403", description = "권한 없음")
    })
    ResponseEntity<CommonResponse<CategoryResponseDto>> updateCategory(
        @Parameter(description = "카테고리 ID", example = "123") Long categoryId,
        RegisterCategoryForm registerCategoryForm
    );

    @Operation(summary = "카테고리 삭제", description = "카테고리를 삭제합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "카테고리 삭제 성공"),
        @ApiResponse(responseCode = "404", description = "카테고리 없음"),
        @ApiResponse(responseCode = "403", description = "권한 없음")
    })
    ResponseEntity<CommonResponse<Void>> deleteCategory(
        @Parameter(description = "카테고리 ID", example = "123") Long categoryId
    );

}
