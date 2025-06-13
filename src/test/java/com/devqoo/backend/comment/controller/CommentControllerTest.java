//package com.devqoo.backend.comment.controller;
//
//import static org.mockito.BDDMockito.any;
//import static org.mockito.BDDMockito.given;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//import com.devqoo.backend.auth.jwt.JwtProvider;
//import com.devqoo.backend.category.entity.Category;
//import com.devqoo.backend.comment.dto.form.RegisterCommentForm;
//import com.devqoo.backend.comment.entity.Comment;
//import com.devqoo.backend.comment.service.CommentService;
//import com.devqoo.backend.common.config.SecurityConfig;
//import com.devqoo.backend.common.exception.BusinessException;
//import com.devqoo.backend.common.exception.ErrorCode;
//import com.devqoo.backend.post.entity.Post;
//import com.devqoo.backend.provider.EntityProvider;
//import com.devqoo.backend.provider.UserFixture;
//import com.devqoo.backend.user.entity.User;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import java.util.stream.Stream;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.Arguments;
//import org.junit.jupiter.params.provider.MethodSource;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.context.annotation.Import;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.bean.override.mockito.MockitoBean;
//import org.springframework.test.web.servlet.MockMvc;
//
//@WebMvcTest({CommentController.class})
//@Import({SecurityConfig.class})
//class CommentControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @MockitoBean
//    private CommentService commentService;
//
//    @MockitoBean
//    private JwtProvider jwtProvider;
//
//    @DisplayName("댓글 생성 테스트")
//    @Nested
//    class CreateCommentTests {
//
//        @DisplayName("POST - 댓글 생성 성공")
//        @Test
//        void createComment_Success() throws Exception {
//            // given
//            RegisterCommentForm form = new RegisterCommentForm(1L, 1L, "Valid content");
//            String json = objectMapper.writeValueAsString(form);
//
//            // Test Fixture Entities
//            User user = UserFixture.createUser();
//            Category category = EntityProvider.createCategory("Test Category");
//            Post post = EntityProvider.createPost(user, category);
//            Comment comment = EntityProvider.createComment(post, user, form.content());
//
//            given(commentService.createComment(any(RegisterCommentForm.class)))
//                .willReturn(comment);
//            // when
//            mockMvc.perform(
//                    post("/api/comments")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(json)
//                ).andDo(print())
//                .andExpectAll(
//                    status().isCreated(),
//                    jsonPath("$.data").value(comment.getCommentId())
//                );
//        }
//
//        @DisplayName("POST - 댓글 생성 validation 실패")
//        @ParameterizedTest
//        @MethodSource("provideInvalidCommentForms")
//        void fail_create_comment_Invalid_RequestBody(Long authorId, Long postId, String content) throws Exception {
//            // given
//            RegisterCommentForm form = new RegisterCommentForm(authorId, postId, content);
//            String requestBody = objectMapper.writeValueAsString(form);
//
//            // when
//            mockMvc.perform(
//                    post("/api/comments")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(requestBody))
//                .andExpect(status().isBadRequest());
//        }
//
//        private static Stream<Arguments> provideInvalidCommentForms() {
//            return Stream.of(
//                Arguments.of(null, 1L, "Valid content"),
//                Arguments.of(1L, null, "Valid content"),
//                Arguments.of(1L, 1L, ""),
//                Arguments.of(1L, 1L, null)
//            );
//        }
//
//        @DisplayName("POST - 댓글 생성 실패, 존재하지 않는 사용자")
//        @Test
//        void fail_create_comment_NotFound_user() throws Exception {
//            // given
//            RegisterCommentForm requestBody = new RegisterCommentForm(999L, 1L, "Valid content");
//            given(commentService.createComment(any(RegisterCommentForm.class)))
//                .willThrow(new BusinessException(ErrorCode.USER_NOT_FOUND));
//            // when
//            mockMvc.perform(
//                    post("/api/comments")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(requestBody))
//                ).andDo(print())
//                .andExpectAll(
//                    status().isNotFound(),
//                    jsonPath("$.errorMessageList[0]").value(ErrorCode.USER_NOT_FOUND.getMessage())
//                );
//        }
//
//        @DisplayName("POST - 댓글 생성 실패, 존재하지 않는 게시글")
//        @Test
//        void fail_create_comment_NotFound_post() throws Exception {
//            // given
//            RegisterCommentForm requestBody = new RegisterCommentForm(1L, 999L, "Valid content");
//            given(commentService.createComment(any(RegisterCommentForm.class)))
//                .willThrow(new BusinessException(ErrorCode.POST_NOT_FOUND));
//            // when
//            mockMvc.perform(
//                    post("/api/comments")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(requestBody))
//                ).andDo(print())
//                .andExpectAll(
//                    status().isNotFound(),
//                    jsonPath("$.errorMessageList[0]").value(ErrorCode.POST_NOT_FOUND.getMessage())
//                );
//        }
//
//        @Nested
//        @DisplayName("댓글 수정")
//        class UpdateComment {
//
//            @DisplayName("PATCH - 댓글 수정 성공")
//            @Test
//            void update_comment() throws Exception {
//                Long commentId = 1L;
//                RegisterCommentForm form = new RegisterCommentForm(1L, 1L, "Updated content");
//                String body = objectMapper.writeValueAsString(form);
//
//                mockMvc.perform(
//                        patch("/api/comments/{commentId}", commentId)
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(body)
//                    ).andDo(print())
//                    .andExpectAll(status().isNoContent());
//            }
//        }
//
//        @DisplayName("PATCH - 댓글 수정 실패, validation 실패")
//        @ParameterizedTest
//        @MethodSource("provideInvalidCommentForms")
//        void fail_update_comment_Invalid_Request(Long authorId, Long postId, String content) throws Exception {
//            // given
//            Long commentId = 1L;
//            RegisterCommentForm form = new RegisterCommentForm(authorId, postId, content);
//            String json = objectMapper.writeValueAsString(form);
//
//            // when
//            mockMvc.perform(
//                    patch("/api/comments/{commentId}", commentId)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(json)
//                ).andDo(print())
//                .andExpect(status().isBadRequest());
//        }
//
//    }
//}
