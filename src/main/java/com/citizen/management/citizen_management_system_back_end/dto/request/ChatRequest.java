package com.citizen.management.citizen_management_system_back_end.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ChatRequest {
    @NotBlank(message = "Vui lòng nhập tin nhắn")
    @Size(max = 5000, message = "Tin nhắn không được vượt quá 5000 ký tự")
    private String message;
    
    private String conversationId;
}
