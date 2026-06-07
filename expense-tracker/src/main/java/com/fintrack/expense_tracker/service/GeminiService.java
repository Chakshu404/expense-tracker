package com.fintrack.expense_tracker.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fintrack.expense_tracker.dto.ExtractedBillDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.util.*;

@Service
public class GeminiService {

    // 🔑 Aapki real API key yahan perfectly configured hai
    private final String API_KEY = System.getenv("GEMINI_API_KEY");
    private final String GEMINI_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=" + API_KEY;

    public ExtractedBillDTO analyzeReceipt(byte[] imageBytes, String contentType) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Base64 mein convert kar rahe hain image ko taaki JSON ke sath bhej sakein
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);

            String promptText = "Analyze this receipt image. Extract the Merchant Name, Date (YYYY-MM-DD), Total Amount, and Tax. " +
                    "Also, categorize this expense into exactly one of these categories based on the merchant/items: " +
                    "Food, Travel, Shopping, Utilities, Medical. Return strictly a valid JSON format, without markdown triple backticks: " +
                    "{\"merchant\": \"\", \"date\": \"\", \"amount\": 0.0, \"tax\": 0.0, \"category\": \"\"}";

            // Gemini API ka standard Request Body structure
            Map<String, Object> inlineData = new HashMap<>();
            inlineData.put("mimeType", contentType);
            inlineData.put("data", base64Image);

            Map<String, Object> part1 = new HashMap<>();
            part1.put("text", promptText);

            Map<String, Object> part2 = new HashMap<>();
            part2.put("inlineData", inlineData);

            Map<String, Object> content = new HashMap<>();
            // ⭐ FIX: Gemini ko batana padta hai ki yeh request 'user' bhej raha hai
            content.put("role", "user"); 
            content.put("parts", Arrays.asList(part1, part2));

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("contents", Arrays.asList(content));

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(GEMINI_URL, entity, String.class);

            // Jackson Library se raw response se text nikalna aur DTO mein parse karna
            ObjectMapper mapper = new ObjectMapper();
            String jsonText = mapper.readTree(response.getBody())
                    .path("candidates").get(0)
                    .path("content").path("parts").get(0)
                    .path("text").asText().trim();

            return mapper.readValue(jsonText, ExtractedBillDTO.class);

        } catch (Exception e) {
            throw new RuntimeException("Gemini AI Processing Failed: " + e.getMessage());
        }
    }
}
