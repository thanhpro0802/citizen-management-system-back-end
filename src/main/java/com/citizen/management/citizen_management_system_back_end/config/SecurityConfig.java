package com.citizen.management.citizen_management_system_back_end.config;

import com.citizen.management.citizen_management_system_back_end.security.jwt.AuthTokenFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private AuthTokenFilter authTokenFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 1. Cấu hình CORS và CSRF
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())

            // 2. Thiết lập Session là STATELESS (vì dùng JWT)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // 3. Phân quyền truy cập (Authorize)
            .authorizeHttpRequests(auth -> auth
                // --- A. PUBLIC ENDPOINTS ---
                // Cho phép phương thức OPTIONS (pre-flight check CORS)
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                // API Đăng nhập, Đăng ký
                .requestMatchers("/api/auth/**", "/api/login/**").permitAll()

                // --- B. USER SPECIFIC (CÔNG DÂN & CÁN BỘ ĐỀU DÙNG ĐƯỢC) ---
                // Rule này phải đặt TRƯỚC các rule chặn quyền CAN_BO
                .requestMatchers(HttpMethod.GET, "/api/ho-khau/cua-toi").authenticated()
                .requestMatchers(HttpMethod.GET, "/api/nhan-khau/cua-toi").authenticated()
                .requestMatchers(HttpMethod.GET, "/api/v1/phan-anh/cua-toi").authenticated()
                .requestMatchers(HttpMethod.POST, "/api/v1/phan-anh").authenticated() // Ai cũng được gửi phản ánh

                // --- C. MANAGEMENT ENDPOINTS (CHỈ CÁN BỘ) ---
                // Quản lý Hộ khẩu & Nhân khẩu (trừ các API /cua-toi đã khai báo ở trên)
                .requestMatchers("/api/ho-khau/**").hasAuthority("CAN_BO")
                .requestMatchers("/api/nhan-khau/**").hasAuthority("CAN_BO")

                // Các chức năng xử lý Phản ánh (Phân công, Xử lý nội bộ, Phản hồi)
                .requestMatchers(
                        "/api/v1/phan-anh/*/phan-cong",
                        "/api/v1/phan-anh/*/xu-ly-noi-bo",
                        "/api/v1/phan-anh/*/phan-hoi",
                        "/api/v1/phan-anh/*/danh-gia" // Ví dụ
                ).hasAuthority("CAN_BO")

                // --- D. DEFAULT ---
                // Tất cả các request còn lại yêu cầu phải đăng nhập
                .anyRequest().authenticated()
            )

            // 4. Xử lý Exception
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint((request, response, authException) -> {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error: Unauthorized");
                })
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Error: Forbidden");
                })
            );

        // 5. Thêm Filter kiểm tra Token
        http.addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Cho phép frontend localhost:3000 và 5173
        configuration.setAllowedOrigins(List.of(
            "http://localhost:3000",
            "http://localhost:5173"
        ));
        
        // Cho phép đầy đủ method
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD"));
        
        // Cho phép đầy đủ header
        configuration.setAllowedHeaders(List.of("*"));
        
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}