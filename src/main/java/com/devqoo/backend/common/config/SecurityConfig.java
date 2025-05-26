package com.devqoo.backend.common.config;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CorsConfigurationSource corsConfigurationSource;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            // csrf 설정
            .csrf(AbstractHttpConfigurer::disable)
            // cors 설정
            .cors(cors -> cors.configurationSource(corsConfigurationSource))
            // 요청 권한 설정 (진행 하며 추가 예정)
            .authorizeHttpRequests(
                authorizeRequests -> authorizeRequests
                    .anyRequest().permitAll()
            )
        ;

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();
        // 모든 Origin 허용
        configuration.addAllowedOriginPattern("*");
        // 모든 Header 허용
        configuration.addAllowedHeader("*");
        configuration.setAllowedMethods(
            List.of("OPTIONS", "GET", "POST", "PUT", "PATCH", "DELETE")
        );

        UrlBasedCorsConfigurationSource urlSource = new UrlBasedCorsConfigurationSource();
        urlSource.registerCorsConfiguration("/**", configuration);

        return urlSource;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
