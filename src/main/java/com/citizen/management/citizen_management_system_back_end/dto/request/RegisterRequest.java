package com.citizen.management.citizen_management_system_back_end.dto.request;

import com.citizen.management.citizen_management_system_back_end.enums.EnumVaiTro;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank(message = "Số CCCD không được để trống")
    @Size(min = 12, max = 12, message = "Số CCCD phải có đúng 12 chữ số")
    @Pattern(regexp = "^[0-9]*$", message = "Số CCCD chỉ được chứa ký tự số")
    private String cccd;

    @NotBlank(message = "Mật khẩu không được để trống")
    @Size(min = 6, max = 100, message = "Mật khẩu phải từ 6 đến 100 ký tự")
    private String password;

    // --- THÊM SỐ ĐIỆN THOẠI ---
    @Pattern(regexp = "(84|0[3|5|7|8|9])+([0-9]{8})\\b", message = "Số điện thoại không hợp lệ (Ví dụ: 0912345678)")
    private String soDienThoai;

    private EnumVaiTro vaiTro = EnumVaiTro.CONG_DAN; // Mặc định là công dân
}