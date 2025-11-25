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
                // 1. Bật CSRF (Cross-Site Request Forgery) protection mặc định.
                // Nếu bạn sử dụng session/cookie-based authentication hoặc phục vụ client browser, hãy giữ CSRF protection.
                // Nếu bạn chỉ dùng stateless token-based authentication (ví dụ JWT), có thể disable CSRF cho các endpoint đó như sau:
                // .csrf(csrf -> csrf.ignoringRequestMatchers("/api/v1/phan-anh"))

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