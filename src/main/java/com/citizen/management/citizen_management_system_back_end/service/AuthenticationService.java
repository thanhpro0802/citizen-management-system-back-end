package com.citizen.management.citizen_management_system_back_end.service;

import com.citizen.management.citizen_management_system_back_end.dto.request.LoginRequest;
import com.citizen.management.citizen_management_system_back_end.dto.request.RegisterRequest;
import com.citizen.management.citizen_management_system_back_end.dto.response.JwtResponse;
import com.citizen.management.citizen_management_system_back_end.entity.TaiKhoan;
import com.citizen.management.citizen_management_system_back_end.repository.TaiKhoanRepository;
import com.citizen.management.citizen_management_system_back_end.security.jwt.JwtUtils;
import com.citizen.management.citizen_management_system_back_end.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthenticationService {

    @Autowired
    private TaiKhoanRepository taiKhoanRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    /**
     * Register a new user
     * @param registerRequest containing username, password, and role
     * @return JwtResponse with token and user details
     * @throws RuntimeException if username already exists
     */
    public JwtResponse registerUser(RegisterRequest registerRequest) {
        // Check if username already exists
        if (taiKhoanRepository.existsByTenDangNhap(registerRequest.getUsername())) {
            throw new RuntimeException("Username is already taken!");
        }

        // Create new user account
        TaiKhoan taiKhoan = new TaiKhoan();
        taiKhoan.setTenDangNhap(registerRequest.getUsername());
        taiKhoan.setMatKhau(passwordEncoder.encode(registerRequest.getPassword()));
        taiKhoan.setVaiTro(registerRequest.getVaiTro());

        // Save user to database
        taiKhoan = taiKhoanRepository.save(taiKhoan);

        // Build UserDetails from the saved user
        UserDetailsImpl userDetails = UserDetailsImpl.build(taiKhoan);

        // Create authentication object
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return createJwtResponse(authentication);
    }

    /**
     * Authenticate user login
     * @param loginRequest containing username and password
     * @return JwtResponse with token and user details
     */
    public JwtResponse loginUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return createJwtResponse(authentication);
    }

    /**
     * Helper method to create JWT response from authentication
     * @param authentication the authentication object
     * @return JwtResponse with token and user details
     */
    private JwtResponse createJwtResponse(Authentication authentication) {
        String jwt = jwtUtils.generateJwtToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), roles);
    }
}
