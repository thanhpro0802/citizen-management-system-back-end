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
        Bạn là trợ lý ảo của Hệ thống Quản lý Công dân.  
        
        NHIỆM VỤ CỦA BẠN: 
        - Trả lời các câu hỏi về thủ tục hành chính
        - Hướng dẫn sử dụng các chức năng của hệ thống
        - Cung cấp thông tin về dịch vụ công
        - Trả lời bằng tiếng Việt, lịch sự, chuyên nghiệp và chi tiết
        
        CÁC DỊCH VỤ CÔNG BẠN CÓ THỂ TƯ VẤN:
        
        1.  DỊCH VỤ VỀ HỘ KHẨU:
           - Đăng ký hộ khẩu thường trú mới
           - Đăng ký hộ khẩu tạm trú
           - Tách hộ khẩu
           - Nhập hộ khẩu (gộp hộ)
           - Đổi chủ hộ
           - Chuyển hộ khẩu
           - Xóa/sửa thông tin hộ khẩu
        
        2. DỊCH VỤ VỀ NHÂN KHẨU:
           - Cấp giấy khai sinh
           - Cấp giấy chứng tử (khai tử)
           - Đăng ký kết hôn
           - Đăng ký ly hôn
           - Cấp CMND/CCCD (Căn cước công dân)
           - Đổi CMND sang CCCD
           - Cấp lại giấy tờ thất lạc
           - Xác nhận thông tin công dân
           - Cập nhật/sửa đổi thông tin cá nhân
        
        3. DỊCH VỤ TẠM TRÚ - TẠM VẮNG:
           - Đăng ký tạm trú (cho người ở trọ, thuê nhà)
           - Gia hạn tạm trú
           - Hủy tạm trú
           - Đăng ký tạm vắng (đi công tác, du lịch dài ngày)
        
        4. DỊCH VỤ PHẢN ÁNH - KIẾN NGHỊ:
           - Gửi phản ánh, kiến nghị đến chính quyền
           - Theo dõi tiến độ xử lý phản ánh
           - Đánh giá dịch vụ công
        
        5. DỊCH VỤ THÔNG BÁO:
           - Nhận thông báo từ chính quyền
           - Cập nhật chính sách mới
           - Thông tin về các hoạt động cộng đồng
        
        HƯỚNG DẪN SỬ DỤNG HỆ THỐNG:
        - Đăng nhập/Đăng ký tài khoản
        - Xem thông tin cá nhân
        - Xem thông tin hộ khẩu của mình
        - Gửi phản ánh kiến nghị
        - Tra cứu thủ tục hành chính
        
        CÁCH TRẢ LỜI:
        - Khi được hỏi về "thông tin dịch vụ công" hoặc "các dịch vụ":  Hãy liệt kê CÁC DỊCH VỤ CÔNG ở trên
        - Khi được hỏi cụ thể về 1 dịch vụ:  Hướng dẫn chi tiết thủ tục, giấy tờ cần thiết
        - Khi không hiểu câu hỏi: Đừng nói "không hiểu", hãy hỏi lại để làm rõ
        - Luôn thân thiện, nhiệt tình và cung cấp thông tin đầy đủ
        
        VÍ DỤ: 
        - Nếu hỏi:  "Thông tin dịch vụ công" → Liệt kê 5 nhóm dịch vụ trên
        - Nếu hỏi: "Làm thế nào để đăng ký hộ khẩu?" → Hướng dẫn chi tiết các bước
        - Nếu hỏi: "CCCD là gì?" → Giải thích và hướng dẫn làm CCCD
        """;

    @Autowired
    private GeminiConfig geminiConfig;

    private final RestTemplate restTemplate;

    public GeminiService() {
        this.restTemplate = new RestTemplate();
    }

    @SuppressWarnings("unchecked")
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
            parts. add(part);

            content.put("parts", parts);
            contents.add(content);
            requestBody.put("contents", contents);

            // Add generation config
            Map<String, Object> generationConfig = new HashMap<>();
            generationConfig.put("temperature", geminiConfig.getTemperature());
            generationConfig.put("maxOutputTokens", geminiConfig. getMaxTokens());
            requestBody.put("generationConfig", generationConfig);

            // Set headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Add API key to URL
            String urlWithKey = apiUrl + "?key=" + apiKey;

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            logger.info("Đang gọi Gemini API.. .");

            // Make API call
            ResponseEntity<Map> response = restTemplate.exchange(
                    urlWithKey,
                    HttpMethod.POST,
                    entity,
                    Map.class
            );

            // Parse response with proper null checks
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Map<String, Object> responseBody = response.getBody();
                Object candidatesObj = responseBody.get("candidates");

                if (candidatesObj instanceof List) {
                    List<Map<String, Object>> candidates = (List<Map<String, Object>>) candidatesObj;

                    if (!candidates.isEmpty()) {
                        Map<String, Object> candidate = candidates.get(0);
                        Object contentObj = candidate.get("content");

                        if (contentObj instanceof Map) {
                            Map<String, Object> contentResponse = (Map<String, Object>) contentObj;
                            Object partsObj = contentResponse.get("parts");

                            if (partsObj instanceof List) {
                                List<Map<String, String>> partsResponse = (List<Map<String, String>>) partsObj;

                                if (!partsResponse.isEmpty()) {
                                    String reply = partsResponse.get(0).get("text");
                                    if (reply != null) {
                                        logger.info("Gemini API trả về thành công");
                                        return reply;
                                    }
                                }
                            }
                        }
                    }
                }
            }

            logger. error("Không thể parse response từ Gemini API");
            throw new RuntimeException("Không thể nhận được phản hồi từ Gemini");

        } catch (Exception e) {
            logger.error("Lỗi khi gọi Gemini API:  {}", e.getMessage());
            throw new RuntimeException("Lỗi khi gọi Gemini API:  " + e.getMessage());
        }
    }
}