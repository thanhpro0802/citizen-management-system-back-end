package com.citizen.management.citizen_management_system_back_end.controller;

import com.citizen.management.citizen_management_system_back_end.dto.request.LoginRequest;
import com.citizen.management.citizen_management_system_back_end.dto.request.RegisterRequest;
import com.citizen.management.citizen_management_system_back_end.dto.response.JwtResponse;
import com.citizen.management.citizen_management_system_back_end.service.AuthenticationService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        try {
            JwtResponse response = authenticationService.registerUser(registerRequest);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            // SỬA: Log theo CCCD
            logger.error("Kích hoạt thất bại cho CCCD {}: {}", registerRequest.getCccd(), e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            JwtResponse response = authenticationService.loginUser(loginRequest);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            // SỬA: Log theo CCCD
            logger.error("Đăng nhập thất bại cho CCCD {}: {}", loginRequest.getCccd(), e.getMessage());
            Map<String, String> error = new HashMap<>();
            // SỬA: Thông báo lỗi chính xác hơn
            error.put("message", "Sai số CCCD hoặc mật khẩu");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
    }
}