package com.citizen.management.citizen_management_system_back_end.config;

import com.citizen.management.citizen_management_system_back_end.security.jwt.AuthTokenFilter;
import com.citizen.management.citizen_management_system_back_end.security.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
    @Autowired UserDetailsServiceImpl userDetailsService;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1. Bật cấu hình CORS và tắt CSRF
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                
                // 2. Thiết lập Session STATELESS
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                
                // 3. Phân quyền
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/v1/tai-khoan/**").hasAnyAuthority("CAN_BO_HO_KHAU", "CAN_BO_NHAN_KHAU", "CAN_BO_PHAN_ANH", "TO_TRUONG", "TO_PHO", "ADMIN")
                        
                        // === [CÔNG DÂN] Các API xem thông tin cá nhân (đặt trước rule của cán bộ) ===
                        .requestMatchers("/api/ho-khau/cua-toi").authenticated()
                        .requestMatchers("/api/nhan-khau/cua-toi").authenticated()
                        .requestMatchers("/api/v1/phan-anh/cua-toi").authenticated()
                        .requestMatchers("/api/chat/**").authenticated()
                        
                        // === [CÁN BỘ] Các API quản lý ===
                        .requestMatchers("/api/nhan-khau/**").hasAnyAuthority("CAN_BO_NHAN_KHAU", "TO_TRUONG", "TO_PHO", "ADMIN")
                        .requestMatchers("/api/ho-khau/**").hasAnyAuthority("CAN_BO_HO_KHAU", "TO_TRUONG", "TO_PHO", "ADMIN")
                        .requestMatchers("/api/statistics/**").hasAnyAuthority("CAN_BO_HO_KHAU", "CAN_BO_NHAN_KHAU", "CAN_BO_PHAN_ANH", "TO_TRUONG", "TO_PHO", "ADMIN")
                        .requestMatchers("/api/v1/phan-anh/*/phan-cong").hasAnyAuthority("TO_TRUONG", "TO_PHO", "ADMIN")
                        .requestMatchers(
                                "/api/v1/phan-anh/*/xu-ly-noi-bo",
                                "/api/v1/phan-anh/*/phan-hoi"
                        ).hasAnyAuthority("CAN_BO_PHAN_ANH", "TO_TRUONG", "TO_PHO", "ADMIN")
                        
                        // Các request còn lại phải đăng nhập
                        .anyRequest().authenticated()
                );

        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // 4. Cấu hình CORS Global
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Cho phép các origin cụ thể (KHÔNG DÙNG "*" KHI allowCredentials=true)
        configuration.setAllowedOrigins(List.of(
            "http://localhost:3000",
            "http://localhost:5173"
        ));

        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD"));
        
        // Liệt kê cụ thể các headers được phép
        configuration.setAllowedHeaders(List.of(
            "Authorization",
            "Content-Type",
            "Accept",
            "Origin",
            "X-Requested-With"
        ));
        
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}