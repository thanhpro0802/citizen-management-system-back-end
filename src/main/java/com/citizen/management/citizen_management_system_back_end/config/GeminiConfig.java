package com.citizen.management.citizen_management_system_back_end.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GeminiConfig {
    
    @Value("${gemini.api.key}")
    private String apiKey;
    
    @Value("${gemini.api.url}")
    private String apiUrl;
    
    @Value("${gemini.model}")
    private String model;
    
    @Value("${gemini.max.tokens}")
    private Integer maxTokens;
    
    @Value("${gemini.temperature}")
    private Double temperature;
    
    public String getApiKey() {
        return apiKey;
    }
    
    public String getApiUrl() {
        return apiUrl;
    }
    
    public String getModel() {
        return model;
    }
    
    public Integer getMaxTokens() {
        return maxTokens;
    }
    
    public Double getTemperature() {
        return temperature;
    }
}
