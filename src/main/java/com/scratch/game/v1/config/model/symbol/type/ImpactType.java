package com.scratch.game.v1.config.model.symbol.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

/**
 * Enum for ImpactType.
 */
public enum ImpactType {
    MULTIPLY_REWARD("multiply_reward"),
    EXTRA_BONUS("extra_bonus"),
    MISS("miss");

    private final String jsonValue;

    ImpactType(String jsonValue) {
        this.jsonValue = jsonValue;
    }

    @JsonValue
    public String getJsonValue() {
        return jsonValue;
    }

    @JsonCreator
    public static ImpactType fromString(String value) {
        return Arrays.stream(values())
                .filter(impact -> impact.jsonValue.equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown impact type: " + value));
    }
}