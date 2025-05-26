package com.devqoo.backend.category.controller;

import com.devqoo.backend.category.dto.form.RegisterCategoryForm;
import com.devqoo.backend.category.dto.response.CategoryResponseDto;
import com.devqoo.backend.category.service.CategoryFacade;
import com.devqoo.backend.common.response.CommonResponse;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
public class CategoryController implements CategoryApiDocs {

    // TODO : API 호출 시 관리자만 호출 할 수 있도록 권한 설정 필요 - 조회 제외
    private final CategoryFacade categoryFacade;

    @PostMapping
    public ResponseEntity<CommonResponse<CategoryResponseDto>> createCategory(
        @RequestBody @Valid RegisterCategoryForm registerCategoryForm
    ) {
        CategoryResponseDto responseDto = categoryFacade.createCategory(registerCategoryForm);
        HttpStatus createStatus = HttpStatus.CREATED;
        CommonResponse<CategoryResponseDto> response = CommonResponse.success(createStatus.value(), responseDto);
        return ResponseEntity.status(createStatus).body(response);
    }

    @GetMapping
    public ResponseEntity<CommonResponse<List<CategoryResponseDto>>> getCategories() {
        List<CategoryResponseDto> categories = categoryFacade.getCategories();
        CommonResponse<List<CategoryResponseDto>> response = CommonResponse.success(HttpStatus.OK.value(), categories);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{categoryId}")
    public ResponseEntity<CommonResponse<CategoryResponseDto>> updateCategory(
        @PathVariable Long categoryId,
        @RequestBody @Valid RegisterCategoryForm registerCategoryForm
    ) {
        CategoryResponseDto categoryResponseDto = categoryFacade.updateCategory(categoryId, registerCategoryForm);
        CommonResponse<CategoryResponseDto> categoryResponse =
            CommonResponse.success(HttpStatus.OK.value(), categoryResponseDto);
        return ResponseEntity.ok(categoryResponse);
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<CommonResponse<Void>> deleteCategory(@PathVariable Long categoryId) {
        // 카테고리 안에 게시글이 존재하면 어떻게 처리할 것인가????????
        categoryFacade.deleteById(categoryId);
        return ResponseEntity.noContent().build();
    }
}
