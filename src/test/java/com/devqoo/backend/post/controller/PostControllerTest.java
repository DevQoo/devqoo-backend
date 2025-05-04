package com.devqoo.backend.post.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.devqoo.backend.category.entity.Category;
import com.devqoo.backend.category.repository.CategoryRepository;
import com.devqoo.backend.post.dto.form.PostRegisterForm;
import com.devqoo.backend.post.entity.Post;
import com.devqoo.backend.post.repository.PostRepository;
import com.devqoo.backend.user.entity.User;
import com.devqoo.backend.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private User testUser;
    private Category testCategory;


    @BeforeEach
    void setUp() {
        testUser = userRepository.save(new User("테스트유저", "test@example.com",
            "asdf", null, "USER"));
        testCategory = categoryRepository.save(new Category("게시판 테스트"));
    }

    @Test
    @DisplayName("POST /api/posts - 게시글 생성")
    void PostCreateApiTest() throws Exception {
        // given
        PostRegisterForm testPostForm = PostRegisterForm.builder()
            .title("테스트 제목")
            .content("테스트 본문")
            .userId(testUser.getUserId())
            .categoryId(testCategory.getCategoryId())
            .build();

        mockMvc.perform(post("/api/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testPostForm)))
            .andExpect(status().isOk());

        // then
        Post saved = postRepository.findAll().get(0);
        Assertions.assertEquals(saved.getTitle(), testPostForm.getTitle());
        Assertions.assertEquals(saved.getContent(), testPostForm.getContent());

    }
}