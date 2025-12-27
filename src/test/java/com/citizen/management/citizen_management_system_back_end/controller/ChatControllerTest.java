package com.citizen.management.citizen_management_system_back_end.controller;

import com.citizen.management.citizen_management_system_back_end.dto.request.ChatRequest;
import com.citizen.management.citizen_management_system_back_end.dto.response.ChatResponse;
import com.citizen.management.citizen_management_system_back_end.service.GeminiService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ChatControllerTest {

    @Mock
    private GeminiService geminiService;

    @InjectMocks
    private ChatController chatController;

    @Test
    void testSendMessage_WithValidRequest_ShouldReturnChatResponse() {
        // Given
        ChatRequest request = new ChatRequest();
        request.setMessage("Làm thế nào để đăng ký hộ khẩu?");
        request.setConversationId("test-conversation-id");
        
        when(geminiService.sendMessage(anyString()))
            .thenReturn("Để đăng ký hộ khẩu, bạn cần...");
        
        // When
        ResponseEntity<?> response = chatController.sendMessage(request);
        
        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof ChatResponse);
        
        ChatResponse chatResponse = (ChatResponse) response.getBody();
        assertEquals("Để đăng ký hộ khẩu, bạn cần...", chatResponse.getReply());
        assertEquals("test-conversation-id", chatResponse.getConversationId());
        assertNotNull(chatResponse.getTimestamp());
    }

    @Test
    void testSendMessage_WithoutConversationId_ShouldGenerateOne() {
        // Given
        ChatRequest request = new ChatRequest();
        request.setMessage("Test message");
        
        when(geminiService.sendMessage(anyString()))
            .thenReturn("Test reply");
        
        // When
        ResponseEntity<?> response = chatController.sendMessage(request);
        
        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ChatResponse chatResponse = (ChatResponse) response.getBody();
        assertNotNull(chatResponse.getConversationId());
        assertFalse(chatResponse.getConversationId().isEmpty());
    }

    @Test
    void testSendMessage_WithApiKeyError_ShouldReturnInternalServerError() {
        // Given
        ChatRequest request = new ChatRequest();
        request.setMessage("Test message");
        
        when(geminiService.sendMessage(anyString()))
            .thenThrow(new RuntimeException("Gemini API key chưa được cấu hình"));
        
        // When
        ResponseEntity<?> response = chatController.sendMessage(request);
        
        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        
        @SuppressWarnings("unchecked")
        Map<String, String> error = (Map<String, String>) response.getBody();
        assertTrue(error.get("message").contains("quản trị viên"));
    }

    @Test
    void testSendMessage_WithNetworkError_ShouldReturnServiceUnavailable() {
        // Given
        ChatRequest request = new ChatRequest();
        request.setMessage("Test message");
        
        when(geminiService.sendMessage(anyString()))
            .thenThrow(new RuntimeException("Lỗi network timeout"));
        
        // When
        ResponseEntity<?> response = chatController.sendMessage(request);
        
        // Then
        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        
        @SuppressWarnings("unchecked")
        Map<String, String> error = (Map<String, String>) response.getBody();
        assertTrue(error.get("message").contains("kết nối mạng"));
    }

    @Test
    void testSendMessage_WithGenericError_ShouldReturnInternalServerError() {
        // Given
        ChatRequest request = new ChatRequest();
        request.setMessage("Test message");
        
        when(geminiService.sendMessage(anyString()))
            .thenThrow(new RuntimeException("Some other error"));
        
        // When
        ResponseEntity<?> response = chatController.sendMessage(request);
        
        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
    }
}
