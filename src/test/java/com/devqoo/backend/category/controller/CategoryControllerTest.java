package com.devqoo.backend.category.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.devqoo.backend.auth.jwt.JwtProvider;
import com.devqoo.backend.category.dto.form.RegisterCategoryForm;
import com.devqoo.backend.category.dto.response.CategoryResponseDto;
import com.devqoo.backend.category.service.CategoryFacade;
import com.devqoo.backend.common.config.SecurityConfig;
import com.devqoo.backend.common.exception.BusinessException;
import com.devqoo.backend.common.exception.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CategoryController.class)
@AutoConfigureMockMvc
@Import({SecurityConfig.class})
class CategoryControllerTest {

    public static final String CATEGORY_NAME = "질문 게시판";
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CategoryFacade categoryFacade;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private JwtProvider jwtProvider;

    @DisplayName("POST /api/categories - 카테고리 생성")
    @Test
    void shouldCreateCategoryAndReturn201() throws Exception {
        // given
        long categoryId = 1L;
        String categoryName = CATEGORY_NAME;
        RegisterCategoryForm form = new RegisterCategoryForm(categoryName);
        CategoryResponseDto responseDto = new CategoryResponseDto(categoryId, categoryName);

        given(categoryFacade.createCategory(any(RegisterCategoryForm.class))).willReturn(responseDto);

        // when && then
        mockMvc.perform(post("/api/categories").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(form))).andExpect(status().isCreated())
            .andExpect(jsonPath("$.data.categoryId").value(categoryId))
            .andExpect(jsonPath("$.data.categoryName").value(categoryName)).andDo(print());
    }

    @DisplayName("POST /api/categories - 카테고리 생성 실패")
    @Test
    void writeHereTestName() throws Exception {
        // given
        String categoryName = CATEGORY_NAME;
        RegisterCategoryForm form = new RegisterCategoryForm(categoryName);

        given(categoryFacade.createCategory(any())).willThrow(
            new BusinessException(ErrorCode.CATEGORY_NAME_DUPLICATED));
        // when && then

        mockMvc.perform(post("/api/categories").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(form))).andExpect(status().isConflict())
            .andExpect(jsonPath("$.errorMessageList[0]").value(ErrorCode.CATEGORY_NAME_DUPLICATED.getMessage()))
            .andDo(print());

    }

    @DisplayName("POST /api/categories - 카테고리 생성 실패 - 잘못된 요청")
    @Test
    void shouldReturn400_whenRequestBodyIsInvalid() throws Exception {
        // given
        RegisterCategoryForm form = new RegisterCategoryForm("");

        // when && then
        mockMvc.perform(post("/api/categories").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(form))).andExpect(status().isBadRequest()).andDo(print());
    }

    @DisplayName("GET /api/categories - 카테고리 목록 조회")
    @Test
    void shouldReturnCategoryList() throws Exception {
        // given
        List<CategoryResponseDto> responseDtos = List.of(new CategoryResponseDto(1L, CATEGORY_NAME),
            new CategoryResponseDto(2L, "자유 게시판"));
        given(categoryFacade.getCategories()).willReturn(responseDtos);

        // when && then
        mockMvc.perform(get("/api/categories").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
            .andExpect(jsonPath("$.data").isArray()).andExpect(jsonPath("$.data.length()").value(2)).andDo(print());

    }

    @DisplayName("PATCH /api/categories/{categoryId} - 카테고리 수정")
    @Test
    void shouldUpdateCategoryNameAndReturn200() throws Exception {
        // given
        long categoryId = 1L;
        String categoryName = CATEGORY_NAME;
        RegisterCategoryForm form = new RegisterCategoryForm(categoryName);
        CategoryResponseDto responseDto = new CategoryResponseDto(categoryId, categoryName);

        given(categoryFacade.updateCategory(
            anyLong(),
            any(RegisterCategoryForm.class)
        )).willReturn(responseDto);

        // when && then
        mockMvc.perform(patch("/api/categories/{categoryId}", categoryId).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(form))).andExpect(status().isOk())
            .andExpect(jsonPath("$.data.categoryId").value(categoryId))
            .andExpect(jsonPath("$.data.categoryName").value(categoryName)).andDo(print());
    }

    @DisplayName("PATCH /api/categories/{categoryId} - 카테고리 수정 실패 - Not Found")
    @Test
    void shouldUpdateCategoryNameAndReturn404() throws Exception {
        // given
        long categoryId = 1L;
        RegisterCategoryForm form = new RegisterCategoryForm(CATEGORY_NAME);
        given(categoryFacade.updateCategory(
            anyLong(),
            any(RegisterCategoryForm.class)
        )).willThrow(new BusinessException(ErrorCode.CATEGORY_NOT_FOUND));

        // when & then
        mockMvc.perform(patch("/api/categories/{categoryId}", categoryId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(form))).andExpect(status().isNotFound())
            .andExpect(jsonPath("$.errorMessageList[0]").value(ErrorCode.CATEGORY_NOT_FOUND.getMessage()))
            .andDo(print());
    }

    @DisplayName("PATCH /api/categories/{categoryId} - 카테고리 수정 실패 - 중복된 카테고리 이름")
    @Test
    void shouldUpdateCategoryNameAndReturn409() throws Exception {
        // given
        long categoryId = 1L;
        RegisterCategoryForm form = new RegisterCategoryForm(CATEGORY_NAME);
        given(categoryFacade.updateCategory(
            anyLong(),
            any(RegisterCategoryForm.class)
        )).willThrow(new BusinessException(ErrorCode.CATEGORY_NAME_DUPLICATED));

        // when & then
        mockMvc.perform(patch("/api/categories/{categoryId}", categoryId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(form))).andExpect(status().isConflict())
            .andExpect(jsonPath("$.errorMessageList[0]").value(ErrorCode.CATEGORY_NAME_DUPLICATED.getMessage()))
            .andDo(print());
    }

    @DisplayName("PATCH /api/categories/{categoryId} - 유효하지 않은 카테고리 이름은 2글자 제한 400 반환")
    @ParameterizedTest
    @ValueSource(strings = {"일"})
    @NullAndEmptySource
    void shouldReturn400_whenRequestIsInvalid(String invalidNewName) throws Exception {
        // given
        Long categoryId = 1L;
        RegisterCategoryForm requestDto = new RegisterCategoryForm(invalidNewName);
        when(categoryFacade.updateCategory(anyLong(), any(RegisterCategoryForm.class)))
            .thenThrow(new BusinessException(ErrorCode.CATEGORY_NAME_DUPLICATED));

        // when & then
        mockMvc.perform(patch("/api/categories/" + categoryId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
            .andExpect(status().isBadRequest())
            .andDo(print());
    }
}
