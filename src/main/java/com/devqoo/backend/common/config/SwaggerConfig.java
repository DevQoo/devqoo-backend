package com.devqoo.backend.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(
                new Info()
                    .title("DevQoo API 문서")
                    .description("Q&A 커뮤니티 프로젝트 API 문서입니다.")
                    .version("0.0.1")
            );
    }
}
