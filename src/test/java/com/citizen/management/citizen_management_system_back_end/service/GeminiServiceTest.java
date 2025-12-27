package com.citizen.management.citizen_management_system_back_end.service;

import com.citizen.management.citizen_management_system_back_end.config.GeminiConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GeminiServiceTest {

    @Mock
    private GeminiConfig geminiConfig;

    @InjectMocks
    private GeminiService geminiService;

    @Test
    void testSendMessage_WithInvalidApiKey_ShouldThrowException() {
        // Given: API key is not configured
        when(geminiConfig.getApiKey()).thenReturn("your-default-key");
        when(geminiConfig.getApiUrl()).thenReturn("https://test-api-url.com");
        
        // When & Then: Should throw exception
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            geminiService.sendMessage("Test message");
        });
        
        assertTrue(exception.getMessage().contains("API key"));
    }

    @Test
    void testSendMessage_WithNullApiKey_ShouldThrowException() {
        // Given: API key is null
        when(geminiConfig.getApiKey()).thenReturn(null);
        when(geminiConfig.getApiUrl()).thenReturn("https://test-api-url.com");
        
        // When & Then: Should throw exception
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            geminiService.sendMessage("Test message");
        });
        
        assertTrue(exception.getMessage().contains("API key"));
    }

    @Test
    void testSendMessage_WithEmptyApiKey_ShouldThrowException() {
        // Given: API key is empty
        when(geminiConfig.getApiKey()).thenReturn("");
        when(geminiConfig.getApiUrl()).thenReturn("https://test-api-url.com");
        
        // When & Then: Should throw exception
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            geminiService.sendMessage("Test message");
        });
        
        assertTrue(exception.getMessage().contains("API key"));
    }
}
