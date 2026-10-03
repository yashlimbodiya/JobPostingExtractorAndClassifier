package org.genAi.project1.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.genAi.project1.model.Content;
import org.genAi.project1.model.Message;
import org.genAi.project1.model.OllamaRequest;
import org.genAi.project1.model.OllamaResponse;
import org.genAi.project1.util.LlmException;

import java.io.IOException;
import java.net.ConnectException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest;
import java.net.http.HttpTimeoutException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;


public class OllamaClient {

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public OllamaClient () {
        this.httpClient = HttpClient.newHttpClient();
        objectMapper = new ObjectMapper();
    }

    public OllamaResponse sendRequest(String systemPrompt, String userPrompt) {

        String baseUrl = System.getenv("LLM_BASE_URL");
        String apiKey = System.getenv("LLM_API_KEY");

        if(baseUrl == null || baseUrl.isEmpty() ) {
            throw new LlmException("Invalid base url." + baseUrl);
        }

        if(apiKey == null || apiKey.isEmpty()) {
            throw new LlmException("Invalid api key" + apiKey);
        }

        HttpResponse<String> apiResponse = null;
        OllamaResponse ollamaResponse = null;


        try {
//            String payload = """
//                {
//                  "model": "%s",
//                  "max_tokens": 500,
//                  "system": "%s",
//                  "messages": [
//                    { "role": "user", "content": "%s" }
//                  ]
//                }""";

            String ollamaModel = System.getenv("LLM_MODEL");

            if(ollamaModel == null || ollamaModel.isEmpty()) {
                throw new LlmException("Invalid LLM model. " + ollamaModel);
            }
            Message userMessage = new Message("user", userPrompt);

            List<Message> messageList = new ArrayList<>();
            messageList.add(userMessage);

            OllamaRequest request = new OllamaRequest(ollamaModel, 500, systemPrompt, messageList);

            HttpRequest apiRequest = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/v1/messages"))
                    .header("anthropic-version", "2023-06-01")
                    .header("content-type", "application/json")
                    .header("x-api-key", apiKey)
                    .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(request)))
                    .timeout(Duration.ofSeconds(100))
                    .build();
            apiResponse = httpClient.send(apiRequest, HttpResponse.BodyHandlers.ofString());
            if(apiResponse == null || apiResponse.body() == null) {
                throw new LlmException("Null response from Ollama API");
            } else if (apiResponse.statusCode() != 200) {
                throw new LlmException("Non 200 response from Ollama API - " + apiResponse.statusCode() + " | message: " + apiResponse.body());
            }

            ollamaResponse = objectMapper.readValue(apiResponse.body(), OllamaResponse.class);
            if(ollamaResponse == null || ollamaResponse.stop_reason() == null) {
                throw new LlmException("Invalid API resonse");
            } else if(!"end_turn".equalsIgnoreCase(ollamaResponse.stop_reason())) {
                throw new LlmException("Ollama model failed, Resaon: " + ollamaResponse.stop_reason());
            }

            List<Content> contentList = ollamaResponse.content();
            if(contentList == null || contentList.isEmpty()) {
                throw new LlmException("Ollama Response error - content field is empty");

            }



        }
        catch (ConnectException ex) {
            throw new LlmException("Ollama model is not running. ", ex);
        }
        catch (HttpTimeoutException ex) {
            throw new LlmException("Ollama model did not respond within 100 seconds. ", ex);
        }
        catch (IOException | InterruptedException ex) {
            throw new LlmException("Something went wrong", ex);
        }

        return ollamaResponse;

    }
}
