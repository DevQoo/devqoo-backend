package com.devqoo.backend.common.config;

import com.devqoo.backend.auth.jwt.JwtAuthenticationEntryPoint;
import com.devqoo.backend.auth.jwt.JwtAuthenticationFilter;
import com.devqoo.backend.auth.jwt.JwtLoginSuccessHandler;
import com.devqoo.backend.auth.jwt.JwtLogoutHandler;
import com.devqoo.backend.auth.jwt.JwtProvider;
import com.devqoo.backend.auth.jwt.JwtService;
import com.devqoo.backend.auth.repository.AuthRepository;
import com.devqoo.backend.auth.security.CustomLoginFailureHandler;
import com.devqoo.backend.auth.security.JsonUsernamePasswordAuthenticationFilter;
import com.devqoo.backend.auth.security.SecurityMatchers;
import com.devqoo.backend.user.enums.UserRoleType;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyAuthoritiesMapper;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    private final CorsConfigurationSource corsConfigurationSource;

    @Bean
    public SecurityFilterChain filterChain(
        HttpSecurity http,
        JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
        DaoAuthenticationProvider daoAuthenticationProvider,
        ObjectMapper objectMapper,
        JwtService jwtService,
        JwtProvider jwtProvider,
        AuthRepository authRepository
    ) throws Exception {

        http
            .authenticationProvider(daoAuthenticationProvider)
            // csrf 설정
            .csrf(AbstractHttpConfigurer::disable)
            // cors 설정
            .cors(cors -> cors.configurationSource(corsConfigurationSource))
            // 요청 권한 설정 (진행 하며 추가 예정)
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers(SecurityMatchers.PUBLIC_MATCHERS).permitAll()
                .anyRequest().hasRole(UserRoleType.STUDENT.name())
            )
            .logout(logout ->
                logout
                    .logoutRequestMatcher(SecurityMatchers.LOGOUT)
                    .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler())
                    .addLogoutHandler(new JwtLogoutHandler(jwtService))
            )
            .with(
                new JsonUsernamePasswordAuthenticationFilter.Configurer(objectMapper),
                configurer ->
                    configurer
                        .successHandler(new JwtLoginSuccessHandler(objectMapper, jwtService))
                        .failureHandler(new CustomLoginFailureHandler(objectMapper))
            )
            .sessionManagement(session ->
                session
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .addFilterBefore(
                new JwtAuthenticationFilter(objectMapper, jwtProvider, authRepository),
                JsonUsernamePasswordAuthenticationFilter.class
            )
        // exception
//            .exceptionHandling(
//                exception -> exception
//                    .authenticationEntryPoint(jwtAuthenticationEntryPoint))
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

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(
        UserDetailsService userDetailsService,
        PasswordEncoder passwordEncoder,
        RoleHierarchy roleHierarchy
    ) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        provider.setAuthoritiesMapper(new RoleHierarchyAuthoritiesMapper(roleHierarchy));
        return provider;
    }

    @Bean
    public RoleHierarchy roleHierarchy() {
        return RoleHierarchyImpl.withDefaultRolePrefix()
            .role(UserRoleType.ADMIN.name())
            .implies(UserRoleType.TEACHER.name(), UserRoleType.MENTOR.name(), UserRoleType.STUDENT.name())

            .role(UserRoleType.TEACHER.name())
            .implies(UserRoleType.STUDENT.name())

            .role(UserRoleType.MENTOR.name())
            .implies(UserRoleType.STUDENT.name())

            .build();
    }


}
