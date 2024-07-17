package com.hmydk.codenamesuggest.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hmydk.codenamesuggest.config.PromptDesign;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
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


    public static String getAIResponse(String apiKey, String language, String textContent) throws Exception {
        textContent = PromptDesign.getPrompt(language, "Variable", textContent);

        HttpURLConnection connection = getHttpURLConnection(apiKey, textContent);
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

        return "";
    }

    private static @NotNull HttpURLConnection getHttpURLConnection(String apiKey, String textContent) throws IOException {
        String apiUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash-latest:generateContent?key=" + apiKey;
        String jsonInputString = "{\"contents\":[{\"parts\":[{\"text\":\"" + textContent + "\"}]}]}";


        // 设置URL
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        // 设置请求方法为POST
        connection.setRequestMethod("POST");

        // 设置请求头
        connection.setRequestProperty("Content-Type", "application/json");

        // 允许发送数据
        connection.setDoOutput(true);
        connection.setConnectTimeout(10000); // 连接超时：10秒
        connection.setReadTimeout(10000); // 读取超时：10秒

        // 写入请求体数据
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }
        return connection;
    }

    public static boolean validateConfig(String model, String apiKey, String language) {
        int statusCode;
        try {
            HttpURLConnection connection = getHttpURLConnection(apiKey, "hi");
            statusCode = connection.getResponseCode();
        } catch (IOException e) {
            return false;
        }
        // 打印状态码
        System.out.println("HTTP Status Code: " + statusCode);
        return statusCode == 200;
    }
}
