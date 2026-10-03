package org.genAi.project1.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record OllamaRequest (
        @JsonProperty("model") String model,
        @JsonProperty("max_tokens") int max_tokens,
        @JsonProperty("system") String system,
        @JsonProperty("messages") List<Message> messages
) {}
