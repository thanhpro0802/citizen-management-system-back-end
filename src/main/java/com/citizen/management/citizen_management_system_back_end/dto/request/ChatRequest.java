package com.citizen.management.citizen_management_system_back_end.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ChatRequest {
    @NotBlank(message = "Vui lòng nhập tin nhắn")
    private String message;
    
    private String conversationId;
}
