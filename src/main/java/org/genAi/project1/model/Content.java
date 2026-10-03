package org.genAi.project1.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Content (
        @JsonProperty("type") String type,
        @JsonProperty("text") String text
) {}
