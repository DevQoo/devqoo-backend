//package com.devqoo.backend.category.controller;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.BDDMockito.given;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//import com.devqoo.backend.category.dto.form.RegisterCategoryForm;
//import com.devqoo.backend.category.dto.response.CategoryResponseDto;
//import com.devqoo.backend.category.service.CategoryFacade;
//import com.devqoo.backend.common.config.SecurityConfig;
//import com.devqoo.backend.common.exception.BusinessException;
//import com.devqoo.backend.common.exception.ErrorCode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.mockito.ArgumentMatchers;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.context.annotation.Import;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.bean.override.mockito.MockitoBean;
//import org.springframework.test.web.servlet.MockMvc;
//
//@WebMvcTest(CategoryController.class)
//@AutoConfigureMockMvc
//@Import({SecurityConfig.class})
//class CategoryControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockitoBean
//    private CategoryFacade categoryFacade;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @DisplayName("POST /api/categories - 카테고리 생성")
//    @Test
//    void shouldCreateCategoryAndReturn201() throws Exception {
//        // given
//        long categoryId = 1L;
//        String categoryName = "질문 게시판";
//        RegisterCategoryForm form = new RegisterCategoryForm(categoryName);
//        CategoryResponseDto responseDto = new CategoryResponseDto(categoryId, categoryName);
//
//        given(categoryFacade.createCategory(ArgumentMatchers.any(RegisterCategoryForm.class)))
//            .willReturn(responseDto);
//
//        // when && then
//        mockMvc.perform(post("/api/categories")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(form)))
//            .andExpect(status().isCreated())
//            .andExpect(jsonPath("$.data.categoryId").value(categoryId))
//            .andExpect(jsonPath("$.data.categoryName").value(categoryName))
//            .andDo(print());
//    }
//
//    @DisplayName("POST /api/categories - 카테고리 생성 실패")
//    @Test
//    void writeHereTestName() throws Exception {
//        // given
//        String categoryName = "질문 게시판";
//        RegisterCategoryForm form = new RegisterCategoryForm(categoryName);
//
//        given(categoryFacade.createCategory(any()))
//            .willThrow(new BusinessException(ErrorCode.CATEGORY_NAME_DUPLICATED));
//        // when && then
//
//        mockMvc.perform(post("/api/categories")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(form)))
//            .andExpect(status().isConflict())
//            .andExpect(jsonPath("$.errorMessageList[0]").value(ErrorCode.CATEGORY_NAME_DUPLICATED.getMessage()))
//            .andDo(print());
//
//    }
//}
