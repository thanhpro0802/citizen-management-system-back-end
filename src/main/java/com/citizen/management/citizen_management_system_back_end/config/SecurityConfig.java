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
                        // Cho phép phương thức OPTIONS (để trình duyệt pre-flight check CORS)
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // Các API Public (Đăng nhập, Đăng ký)
                        .requestMatchers("/api/auth/**", "/api/login/**").permitAll()

                        // === QUAN TRỌNG: Rule cụ thể phải đặt TRƯỚC rule tổng quát ===

                        // [Riêng] Xem hộ khẩu của chính mình -> Đăng nhập là xem được (USER/CAN_BO đều được)
                        .requestMatchers(HttpMethod.GET, "/api/ho-khau/cua-toi").authenticated()

                        // [Chung] Các API quản lý Hộ khẩu/Nhân khẩu khác -> Chỉ CÁN BỘ
                        .requestMatchers("/api/ho-khau/**").hasAuthority("CAN_BO")
                        .requestMatchers("/api/nhan-khau/**").hasAuthority("CAN_BO")

                        // Các API xử lý Phản ánh (Phân công, Xử lý, Phản hồi) -> Chỉ CÁN BỘ
                        .requestMatchers(
                                "/api/v1/phan-anh/*/phan-cong",
                                "/api/v1/phan-anh/*/xu-ly-noi-bo",
                                "/api/v1/phan-anh/*/phan-hoi"
                        ).hasAuthority("CAN_BO")

                        // Tất cả các request còn lại -> Chỉ cần Đăng nhập
                        .anyRequest().authenticated()
                )

                // 4. Xử lý Exception (401 Unauthorized, 403 Forbidden)
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error: Unauthorized");
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Error: Forbidden");
                        })
                );

        // 5. Thêm Filter kiểm tra Token trước
        http.addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Cho phép frontend localhost:3000 gọi API
        configuration.setAllowedOrigins(List.of("http://localhost:3000"));
        // Cho phép đầy đủ các method
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD"));
        // Cho phép mọi header
        configuration.setAllowedHeaders(List.of("*"));
        // Cho phép gửi credentials (nếu cần cookie/auth header)
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}