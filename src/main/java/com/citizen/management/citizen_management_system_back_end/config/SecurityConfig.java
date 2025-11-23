package com.citizen.management.citizen_management_system_back_end.config; // Đảm bảo package đúng

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. Tắt CSRF (Cross-Site Request Forgery)
                // (Vì chúng ta đang dùng API, không dùng form HTML truyền thống)
                .csrf(AbstractHttpConfigurer::disable)

                // 2. Cấu hình phân quyền (Authorization)
                .authorizeHttpRequests(authorize -> authorize

                        // 2.1. Cho phép (permit) API gửi phản ánh
                        .requestMatchers(HttpMethod.POST, "/api/v1/phan-anh").permitAll()

                        // 2.2. (Tạm thời) Cho phép tất cả các API khác để test
                        // TODO: Sau này bạn sẽ xóa dòng này và cấu hình chi tiết hơn
                        .anyRequest().permitAll()
                );

        return http.build();
    }
}