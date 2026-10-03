package org.genAi.project1.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Usage(
        @JsonProperty("input_tokens") int inputTokens,
        @JsonProperty("cache_read_input_tokens") int cacheReadInputTokens,
        @JsonProperty("output_tokens") int outputTokens) {}
