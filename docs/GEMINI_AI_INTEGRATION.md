# Tích hợp Google Gemini AI Chatbot

## Tổng quan
Hệ thống đã được tích hợp với Google Gemini AI để cung cấp trợ lý ảo giúp người dùng:
- Trả lời câu hỏi về thủ tục hành chính
- Hướng dẫn sử dụng các chức năng của hệ thống
- Cung cấp thông tin về dịch vụ công
- Trả lời bằng tiếng Việt một cách lịch sự và chuyên nghiệp

## Kiến trúc

Tích hợp này sử dụng:
- **Spring RestTemplate** để gọi Gemini API REST endpoint
- **Không cần thêm dependencies** bên ngoài - chỉ sử dụng Spring Boot Web có sẵn
- **Environment variables** để quản lý API key an toàn

## Cấu hình

### 1. Lấy API Key từ Google

1. Truy cập [Google AI Studio](https://makersuite.google.com/app/apikey)
2. Đăng nhập bằng tài khoản Google
3. Tạo API key mới
4. Sao chép API key để sử dụng

### 2. Cấu hình Environment Variables

Thêm biến môi trường `GEMINI_API_KEY` với giá trị API key bạn vừa lấy:

**Trên Linux/Mac:**
```bash
export GEMINI_API_KEY=your_api_key_here
```

**Trên Windows:**
```cmd
set GEMINI_API_KEY=your_api_key_here
```

Hoặc thêm vào file `.env` (nếu sử dụng):
```
GEMINI_API_KEY=your_api_key_here
```

### 3. Cấu hình trong application.properties

File `application.properties` đã được cấu hình sẵn:
```properties
# Gemini API Configuration
gemini.api.key=${GEMINI_API_KEY:your-default-key}
gemini.api.url=https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent
gemini.model=gemini-pro
gemini.max.tokens=1000
gemini.temperature=0.7
```

Bạn có thể tùy chỉnh:
- `gemini.max.tokens`: Số token tối đa trong response (mặc định: 1000)
- `gemini.temperature`: Độ sáng tạo của AI (0.0 - 1.0, mặc định: 0.7)

## API Endpoint

### POST `/api/chat/message`

Gửi tin nhắn đến chatbot AI và nhận phản hồi.

**Authentication:** Required (JWT Token)

**Request Body:**
```json
{
  "message": "Làm thế nào để đăng ký hộ khẩu mới?",
  "conversationId": "optional-conversation-id"
}
```

**Response (Success - 200 OK):**
```json
{
  "reply": "Để đăng ký hộ khẩu mới, bạn cần...",
  "conversationId": "uuid-generated-or-provided",
  "timestamp": "2025-12-27T06:00:00"
}
```

**Response (Error):**
```json
{
  "message": "Mô tả lỗi"
}
```

**Possible HTTP Status Codes:**
- `200 OK`: Thành công
- `400 Bad Request`: Tin nhắn không hợp lệ (quá dài, trống)
- `401 Unauthorized`: Chưa đăng nhập
- `500 Internal Server Error`: Lỗi cấu hình hoặc lỗi hệ thống
- `503 Service Unavailable`: Lỗi kết nối mạng

## Ví dụ sử dụng

### Curl
```bash
curl -X POST http://localhost:8080/api/chat/message \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "message": "Hướng dẫn tôi cách đăng ký tạm trú"
  }'
```

### JavaScript (Fetch API)
```javascript
const response = await fetch('http://localhost:8080/api/chat/message', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
    'Authorization': `Bearer ${jwtToken}`
  },
  body: JSON.stringify({
    message: 'Hướng dẫn tôi cách đăng ký tạm trú',
    conversationId: 'optional-id'
  })
});

const data = await response.json();
console.log(data.reply);
```

## Bảo mật

- ✅ Input validation: Giới hạn độ dài tin nhắn tối đa 5000 ký tự
- ✅ Authentication required: Chỉ người dùng đã đăng nhập mới sử dụng được
- ✅ API key được lưu trong environment variables, không hard-code
- ✅ Error handling đầy đủ, không expose thông tin nhạy cảm
- ✅ Logging để theo dõi và debugging

## Giới hạn

- Độ dài tin nhắn tối đa: 5000 ký tự
- Rate limiting: Chưa implement (cần thêm nếu cần thiết)
- Conversation history: Chưa lưu trữ lịch sử hội thoại (có thể mở rộng sau)

## Troubleshooting

### Lỗi "Gemini API key chưa được cấu hình"
- Kiểm tra biến môi trường `GEMINI_API_KEY` đã được set chưa
- Restart ứng dụng sau khi set biến môi trường

### Lỗi "Không thể xử lý yêu cầu"
- Kiểm tra API key có hợp lệ không
- Kiểm tra kết nối internet
- Xem log để biết chi tiết lỗi

### Lỗi 401 Unauthorized
- Đảm bảo đã gửi JWT token trong header Authorization
- Token phải có format: `Bearer <token>`

## Mở rộng trong tương lai

- [ ] Lưu trữ lịch sử hội thoại
- [ ] Rate limiting để tránh abuse
- [ ] Streaming response cho trải nghiệm real-time
- [ ] Context awareness dựa trên thông tin user
- [ ] Multi-language support
