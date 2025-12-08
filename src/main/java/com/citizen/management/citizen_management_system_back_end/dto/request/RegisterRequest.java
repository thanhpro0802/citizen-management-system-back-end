package com.citizen.management.citizen_management_system_back_end.dto.request;

import com.citizen.management.citizen_management_system_back_end.enums.EnumVaiTro;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters")
    private String password;

    private EnumVaiTro vaiTro = EnumVaiTro.CONG_DAN; // Default role is citizen
}
