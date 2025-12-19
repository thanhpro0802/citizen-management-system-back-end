package com.citizen.management.citizen_management_system_back_end.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager; // [Mới]
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration; // [Mới]
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // 1. Bean Mã hóa mật khẩu (Đã thêm ở bước trước)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 2. Bean Quản lý xác thực (ĐÂY LÀ CÁI BẠN ĐANG THIẾU DẪN ĐẾN LỖI)
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Kích hoạt CORS
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // Tắt CSRF
                .csrf(csrf -> csrf.disable())

                // Phân quyền
                .authorizeHttpRequests(auth -> auth
                        // Cho phép OPTIONS đi qua (fix lỗi 403 Preflight của React)
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // Các API Public
                        .requestMatchers("/api/auth/**", "/api/login/**").permitAll()

                        // Cho phép tất cả các request khác (để bạn test cho dễ)
                        .anyRequest().permitAll()
                );

        return http.build();
    }

    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Cho phép Frontend
        configuration.setAllowedOrigins(List.of("http://localhost:3000"));

        // Cho phép các method
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD"));

        // Cho phép mọi Header
        configuration.setAllowedHeaders(List.of("*"));

        // Cho phép Credentials
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}