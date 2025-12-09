package com.citizen.management.citizen_management_system_back_end.service;

import com.citizen.management.citizen_management_system_back_end.dto.request.LoginRequest;
import com.citizen.management.citizen_management_system_back_end.dto.request.RegisterRequest;
import com.citizen.management.citizen_management_system_back_end.dto.response.JwtResponse;
import com.citizen.management.citizen_management_system_back_end.entity.NhanKhau;
import com.citizen.management.citizen_management_system_back_end.entity.TaiKhoan;
import com.citizen.management.citizen_management_system_back_end.repository.NhanKhauRepository;
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
    private NhanKhauRepository nhanKhauRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    public JwtResponse registerUser(RegisterRequest registerRequest) {
        // 1. Kiểm tra tồn tại tài khoản
        if (taiKhoanRepository.existsByCccd(registerRequest.getCccd())) {
            throw new RuntimeException("Tài khoản với số CCCD này đã tồn tại!");
        }

        // 2. Kiểm tra tồn tại công dân
        NhanKhau congDan = nhanKhauRepository.findBySoCCCD(registerRequest.getCccd())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy dữ liệu công dân với số CCCD này!"));

        // 3. Tạo tài khoản mới
        TaiKhoan taiKhoan = new TaiKhoan();
        taiKhoan.setCccd(registerRequest.getCccd());
        taiKhoan.setMatKhau(passwordEncoder.encode(registerRequest.getPassword()));

        // --- THÊM DÒNG NÀY: Lưu số điện thoại ---
        taiKhoan.setSoDienThoai(registerRequest.getSoDienThoai());

        taiKhoan.setVaiTro(registerRequest.getVaiTro());

        // Link với NhanKhau
        taiKhoan.setNhanKhau(congDan);

        // Lưu vào DB
        taiKhoan = taiKhoanRepository.save(taiKhoan);

        // Tự động đăng nhập luôn sau khi đăng ký
        UserDetailsImpl userDetails = UserDetailsImpl.build(taiKhoan);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return createJwtResponse(authentication);
    }

    public JwtResponse loginUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getCccd(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return createJwtResponse(authentication);
    }

    private JwtResponse createJwtResponse(Authentication authentication) {
        String jwt = jwtUtils.generateJwtToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), roles);
    }
}