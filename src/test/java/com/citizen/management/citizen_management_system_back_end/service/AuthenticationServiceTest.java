package com.citizen.management.citizen_management_system_back_end.service;

import com.citizen.management.citizen_management_system_back_end.dto.request.LoginRequest;
import com.citizen.management.citizen_management_system_back_end.dto.response.JwtResponse;
import com.citizen.management.citizen_management_system_back_end.entity.TaiKhoan;
import com.citizen.management.citizen_management_system_back_end.repository.TaiKhoanRepository;
import com.citizen.management.citizen_management_system_back_end.security.jwt.JwtUtils;
import com.citizen.management.citizen_management_system_back_end.security.services.UserDetailsImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtils jwtUtils;

    // --- THÊM MOCK NÀY ĐỂ SỬA LỖI NULL POINTER ---
    @Mock
    private TaiKhoanRepository taiKhoanRepository;

    @InjectMocks
    private AuthenticationService authenticationService;

    @Test
    public void testLoginUser_ShouldReturnPhoneNumber_WhenLoginSuccess() {
        // 1. CHUẨN BỊ DỮ LIU GIẢ (MOCK DATA)
        String mockCccd = "001200000001";
        String mockPhone = "0912345678";
        String mockToken = "mock-jwt-token";

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setCccd(mockCccd);
        loginRequest.setPassword("password123");

        // --- GIẢ LẬP TAI KHOAN TỪ DB (Để qua mặt đoạn debug trong Service) ---
        TaiKhoan mockTaiKhoanEntity = new TaiKhoan();
        mockTaiKhoanEntity.setCccd(mockCccd);
        mockTaiKhoanEntity.setSoDienThoai(mockPhone);

        // Khi Service gọi repository, trả về mock entity này
        when(taiKhoanRepository.findByCccd(mockCccd)).thenReturn(Optional.of(mockTaiKhoanEntity));
        // --------------------------------------------------------------------

        // Giả lập UserDetailsImpl trả về từ AuthenticationManager
        UserDetailsImpl mockUserDetails = new UserDetailsImpl(
                "user-id-1",
                mockCccd,
                "encoded-password",
                mockPhone,
                "Nguyen Van A",
                "test@email.com",
                Collections.singletonList(new SimpleGrantedAuthority("CAN_BO"))
        );

        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(mockUserDetails);

        // 2. GIẢ LẬP HÀNH VI
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        when(jwtUtils.generateJwtToken(authentication)).thenReturn(mockToken);

        // 3. CHẠY HÀM CẦN TEST
        JwtResponse response = authenticationService.loginUser(loginRequest);

        // 4. KIỂM TRA KẾT QUẢ
        Assertions.assertNotNull(response);
        Assertions.assertEquals(mockCccd, response.getCccd());

        System.out.println("Test Result - SĐT: " + response.getSoDienThoai());
        Assertions.assertEquals(mockPhone, response.getSoDienThoai(), "Lỗi: Số điện thoại không khớp!");
    }
}