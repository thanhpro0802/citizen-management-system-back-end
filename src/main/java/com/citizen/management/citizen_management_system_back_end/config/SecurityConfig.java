package com.citizen.management.citizen_management_system_back_end.config;

import com.citizen.management.citizen_management_system_back_end.security.jwt.AuthTokenFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer; // Import mới
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;
import org.springframework.web.cors.CorsConfiguration; // Import mới
import org.springframework.web.cors.CorsConfigurationSource; // Import mới
import org.springframework.web.cors.UrlBasedCorsConfigurationSource; // Import mới

import java.util.Arrays; // Import mới
import java.util.List;   // Import mới

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
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1. Bật cấu hình CORS tại đây
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/api/auth/**", "/api/login/**").permitAll()

                        // === QUAN TRỌNG: Đặt rule cụ thể CHO /cua-toi TRƯỚC /api/ho-khau/** ===
                        .requestMatchers(HttpMethod.GET, "/api/ho-khau/cua-toi").authenticated()

                        // Các API quản lý hộ khẩu (thêm, sửa, xóa, tách, nhập, đổi chủ hộ...) chỉ cho cán bộ
                        .requestMatchers("/api/ho-khau/**").hasAnyAuthority("CAN_BO")

                        // Các request còn lại cần đăng nhập
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/nhan-khau/**", "/api/ho-khau/**").hasAuthority("CAN_BO")
                        .requestMatchers("/api/v1/phan-anh/*/phan-cong",
                                "/api/v1/phan-anh/*/xu-ly-noi-bo",
                                "/api/v1/phan-anh/*/phan-hoi").hasAuthority("CAN_BO")
                        .anyRequest().authenticated()
                )
                // Xử lý lỗi 403, 401 để dễ debug
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized: " + authException.getMessage());
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden: " + accessDeniedException.getMessage());
                        }));

        http.addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Cho phép frontend localhost:3000 gọi API
        configuration.setAllowedOrigins(List.of("http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}

    // 2. Thêm Bean cấu hình CORS global
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Cho phép frontend từ localhost:3000
        configuration.setAllowedOrigins(List.of("http://localhost:3000"));
        // Cho phép các phương thức
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        // Cho phép tất cả các headers (Authorization, Content-Type, v.v.)
        configuration.setAllowedHeaders(List.of("*"));
        // Cho phép gửi credentials (nếu cần thiết sau này)
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}