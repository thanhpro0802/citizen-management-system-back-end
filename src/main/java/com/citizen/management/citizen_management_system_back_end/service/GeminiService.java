package com.citizen.management.citizen_management_system_back_end.service;

import com.citizen.management.citizen_management_system_back_end.config.GeminiConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.*;

@Service
public class GeminiService {
    
    private static final Logger logger = LoggerFactory.getLogger(GeminiService.class);
    
    private static final String SYSTEM_PROMPT = """
        Bạn là trợ lý ảo của hệ thống quản lý công dân. 
        Nhiệm vụ của bạn là:
        - Trả lời các câu hỏi về thủ tục hành chính
        - Hướng dẫn sử dụng các chức năng của hệ thống
        - Cung cấp thông tin về dịch vụ công
        - Trả lời bằng tiếng Việt, lịch sự và chuyên nghiệp
        
        Hệ thống quản lý công dân bao gồm các chức năng:
        - Quản lý hộ khẩu: thêm, sửa, xóa, tách hộ, nhập hộ, đổi chủ hộ
        - Quản lý nhân khẩu: thêm, sửa, xóa thông tin công dân
        - Quản lý tạm trú, tạm vắng
        - Phản ánh kiến nghị từ công dân
        - Thông báo từ chính quyền
        """;
    
    @Autowired
    private GeminiConfig geminiConfig;
    
    private final RestTemplate restTemplate;
    
    public GeminiService() {
        this.restTemplate = new RestTemplate();
    }
    
    public String sendMessage(String userMessage) {
        try {
            String apiKey = geminiConfig.getApiKey();
            String apiUrl = geminiConfig.getApiUrl();
            
            // Validate API key
            if (apiKey == null || apiKey.isEmpty() || apiKey.equals("your-default-key")) {
                logger.error("Gemini API key không hợp lệ");
                throw new RuntimeException("Gemini API key chưa được cấu hình");
            }
            
            // Combine system prompt with user message
            String fullMessage = SYSTEM_PROMPT + "\n\nCâu hỏi: " + userMessage;
            
            // Build request body
            Map<String, Object> requestBody = new HashMap<>();
            
            // Create contents array
            List<Map<String, Object>> contents = new ArrayList<>();
            Map<String, Object> content = new HashMap<>();
            
            List<Map<String, String>> parts = new ArrayList<>();
            Map<String, String> part = new HashMap<>();
            part.put("text", fullMessage);
            parts.add(part);
            
            content.put("parts", parts);
            contents.add(content);
            requestBody.put("contents", contents);
            
            // Add generation config
            Map<String, Object> generationConfig = new HashMap<>();
            generationConfig.put("temperature", geminiConfig.getTemperature());
            generationConfig.put("maxOutputTokens", geminiConfig.getMaxTokens());
            requestBody.put("generationConfig", generationConfig);
            
            // Set headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            // Add API key to URL
            String urlWithKey = apiUrl + "?key=" + apiKey;
            
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            
            logger.info("Đang gọi Gemini API...");
            
            // Make API call
            ResponseEntity<Map> response = restTemplate.exchange(
                urlWithKey,
                HttpMethod.POST,
                entity,
                Map.class
            );
            
            // Parse response
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Map<String, Object> responseBody = response.getBody();
                List<Map<String, Object>> candidates = (List<Map<String, Object>>) responseBody.get("candidates");
                
                if (candidates != null && !candidates.isEmpty()) {
                    Map<String, Object> candidate = candidates.get(0);
                    Map<String, Object> contentResponse = (Map<String, Object>) candidate.get("content");
                    List<Map<String, String>> partsResponse = (List<Map<String, String>>) contentResponse.get("parts");
                    
                    if (partsResponse != null && !partsResponse.isEmpty()) {
                        String reply = partsResponse.get(0).get("text");
                        logger.info("Gemini API trả về thành công");
                        return reply;
                    }
                }
            }
            
            logger.error("Không thể parse response từ Gemini API");
            throw new RuntimeException("Không thể nhận được phản hồi từ Gemini");
            
        } catch (Exception e) {
            logger.error("Lỗi khi gọi Gemini API: {}", e.getMessage());
            throw new RuntimeException("Lỗi khi gọi Gemini API: " + e.getMessage());
        }
    }
}
