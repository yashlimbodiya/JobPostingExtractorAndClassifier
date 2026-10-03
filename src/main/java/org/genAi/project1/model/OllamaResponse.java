package org.genAi.project1.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record OllamaResponse (
        @JsonProperty("id") String id,
        @JsonProperty("type") String type,
        @JsonProperty("role") String role,
        @JsonProperty("model") String model,
        @JsonProperty("content") List<Content> content,
        @JsonProperty("stop_reason") String stop_reason,
        @JsonProperty("usage") Usage usage
) {}
