package com.citizen.management.citizen_management_system_back_end.controller;

import com.citizen.management.citizen_management_system_back_end.dto.request.ChatRequest;
import com.citizen.management.citizen_management_system_back_end.dto.response.ChatResponse;
import com.citizen.management.citizen_management_system_back_end.service.GeminiService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/chat")
public class ChatController {
    
    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);
    
    @Autowired
    private GeminiService geminiService;
    
    @PostMapping("/message")
    public ResponseEntity<?> sendMessage(@Valid @RequestBody ChatRequest request) {
        try {
            logger.info("Nhận tin nhắn từ người dùng: {}", request.getMessage());
            
            // Validate input
            if (request.getMessage().length() > 5000) {
                Map<String, String> error = new HashMap<>();
                error.put("message", "Tin nhắn quá dài. Vui lòng nhập tin nhắn ngắn hơn 5000 ký tự");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }
            
            // Get response from Gemini
            String reply = geminiService.sendMessage(request.getMessage());
            
            // Generate conversation ID if not provided
            String conversationId = request.getConversationId();
            if (conversationId == null || conversationId.isEmpty()) {
                conversationId = UUID.randomUUID().toString();
            }
            
            // Build response
            ChatResponse response = new ChatResponse(
                reply,
                conversationId,
                LocalDateTime.now()
            );
            
            logger.info("Đã gửi phản hồi cho conversation: {}", conversationId);
            return ResponseEntity.ok(response);
            
        } catch (RuntimeException e) {
            logger.error("Lỗi xử lý tin nhắn: {}", e.getMessage());
            Map<String, String> error = new HashMap<>();
            
            // Check specific error types
            if (e.getMessage().contains("API key")) {
                error.put("message", "Lỗi cấu hình hệ thống. Vui lòng liên hệ quản trị viên");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
            } else if (e.getMessage().contains("network") || e.getMessage().contains("timeout")) {
                error.put("message", "Lỗi kết nối mạng. Vui lòng thử lại sau");
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(error);
            } else {
                error.put("message", "Không thể xử lý yêu cầu. Vui lòng thử lại sau");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
            }
        }
    }
}
