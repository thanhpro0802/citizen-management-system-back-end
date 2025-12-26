package com.citizen.management.citizen_management_system_back_end.security.jwt;

import com.citizen.management.citizen_management_system_back_end.security.services.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AuthTokenFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getServletPath();

        // 🔥 BỎ QUA JWT FILTER CHO AUTH API
        if (path.startsWith("/api/auth")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String jwt = parseJwt(request);
            System.out.println("1. Token from Header: " + jwt);

            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                System.out.println("2. Token is Valid");

                // 1. Lấy Username
                String username = jwtUtils.getUserNameFromJwtToken(jwt);

                // 2. Lấy Roles từ Token
                List<String> roles = jwtUtils.getRolesFromJwtToken(jwt);
                System.out.println("3. Roles from Token: " + roles);

                // 3. Chuyển đổi roles (String) thành Authorities (Spring Security)
                List<SimpleGrantedAuthority> authorities = roles.stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

                // 4. Load thông tin user (để lấy password, enable status...)
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // 5. [ĐÃ SỬA]: Tạo Authentication bằng danh sách authorities lấy từ Token
                // (Thay vì dùng userDetails.getAuthorities() có thể bị rỗng nếu load lazy)
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        authorities); // <--- Dùng biến này mới đúng

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 6. Set Authentication vào Context
                SecurityContextHolder.getContext().setAuthentication(authentication);
                System.out.println("4. Authentication set successfully");
            } else {
                System.out.println("2. Token is NULL or INVALID");
            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication", e);
        }

        filterChain.doFilter(request, response);
    }


    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }
}