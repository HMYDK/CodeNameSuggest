package com.hmydk.codenamesuggest.util;

import org.json.JSONArray;
import org.json.JSONObject;

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
        String responseText = getAIResponse("AIzaSyAJjqnV4S3oatR4x-lenL96mXwxtzc6z5U", textContent);
        System.out.println("Response Text: " + responseText);
    }

    public static String getAIResponse(String apiKey, String textContent) {
        textContent = PromptDesign.getPrompt("English", "Variable", textContent);
        System.out.println(textContent);

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

            // 获取响应码
            int responseCode = connection.getResponseCode();
            System.out.println(responseCode);

            // 读取响应并打印
            StringBuilder response = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
            }

            // 解析JSON响应
            JSONObject jsonResponse = new JSONObject(response.toString());
            JSONArray candidates = jsonResponse.getJSONArray("candidates");
            if (!candidates.isEmpty()) {
                JSONObject firstCandidate = candidates.getJSONObject(0);
                JSONObject content = firstCandidate.getJSONObject("content");
                JSONArray parts = content.getJSONArray("parts");
                if (!parts.isEmpty()) {
                    JSONObject firstPart = parts.getJSONObject(0);
                    return firstPart.getString("text");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
