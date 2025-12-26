package com.citizen.management.citizen_management_system_back_end.config;

import com.citizen.management.citizen_management_system_back_end.security.jwt.AuthTokenFilter;
import com.citizen.management.citizen_management_system_back_end.security.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer; // Import mới
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration; // Import mới
import org.springframework.web.cors.CorsConfigurationSource; // Import mới
import org.springframework.web.cors.UrlBasedCorsConfigurationSource; // Import mới

import java.util.Arrays; // Import mới
import java.util.List;   // Import mới

@Configuration
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
                // 1. Bật cấu hình CORS tại đây
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        // Cho phép CONG_DAN truy cập /api/nhan-khau/cua-toi
                        .requestMatchers("/api/nhan-khau/cua-toi").authenticated()
                        // Các API khác của nhan-khau và ho-khau chỉ cho CAN_BO
                        .requestMatchers("/api/nhan-khau/**", "/api/ho-khau/**").hasAuthority("CAN_BO")
                        .requestMatchers("/api/v1/phan-anh/*/phan-cong",
                                "/api/v1/phan-anh/*/xu-ly-noi-bo",
                                "/api/v1/phan-anh/*/phan-hoi").hasAuthority("CAN_BO")
                        .anyRequest().authenticated()
                );

        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // 2. Thêm Bean cấu hình CORS global
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Cho phép các origin từ frontend
        configuration.setAllowedOrigins(List.of(
            "http://localhost:3000",
            "http://localhost:5173"
        ));

        configuration.setAllowedMethods(
                List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")
        );
        
        // Liệt kê cụ thể các headers được phép
        configuration.setAllowedHeaders(List.of(
            "Authorization",
            "Content-Type",
            "Accept",
            "Origin",
            "X-Requested-With"
        ));
        
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}