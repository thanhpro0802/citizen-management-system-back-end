package com.citizen.management.citizen_management_system_back_end.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank(message = "Vui lòng nhập số CCCD")
    private String cccd;

    @NotBlank(message = "Vui lòng nhập mật khẩu")
    private String password;
}