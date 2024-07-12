package com.hmydk.codenamesuggest.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hmydk.codenamesuggest.config.PromptDesign;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * AIRequestUtil
 *
 * @author hmydk
 */
public class AIRequestUtil {
    public static void main(String[] args) {
        String textContent = "逻辑删除";
        String responseText = getAIResponse("123", textContent);
        System.out.println("Response Text: " + responseText);
    }

    public static String getAIResponse(String apiKey, String textContent) {
        textContent = PromptDesign.getPrompt("English", "Variable", textContent);

        String apiUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash-latest:generateContent?key=" + apiKey;
        String jsonInputString = "{\"contents\":[{\"parts\":[{\"text\":\"" + textContent + "\"}]}]}";

        try {
            // 设置URL
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // 设置请求方法为POST
            connection.setRequestMethod("POST");

            // 设置请求头
            connection.setRequestProperty("Content-Type", "application/json");

            // 允许发送数据
            connection.setDoOutput(true);

            // 写入请求体数据
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
            // 读取响应并打印
            StringBuilder response = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
            }

            // 使用Jackson解析JSON响应
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonResponse = objectMapper.readTree(response.toString());
            JsonNode candidates = jsonResponse.path("candidates");
            if (candidates.isArray() && candidates.size() > 0) {
                JsonNode firstCandidate = candidates.get(0);
                JsonNode content = firstCandidate.path("content");
                JsonNode parts = content.path("parts");
                if (parts.isArray() && parts.size() > 0) {
                    JsonNode firstPart = parts.get(0);
                    return firstPart.path("text").asText();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
