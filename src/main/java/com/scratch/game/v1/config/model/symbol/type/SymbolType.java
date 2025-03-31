package com.scratch.game.v1.config.model.symbol.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

/**
 * Enum for SymbolTypes.
 */
public enum SymbolType {
    STANDARD("standard"),
    BONUS("bonus");

    private final String jsonValue;

    SymbolType(String jsonValue) {
        this.jsonValue = jsonValue;
    }

    @JsonValue
    public String getJsonValue() {
        return jsonValue;
    }

    @JsonCreator
    public static SymbolType fromString(String value) {
        return Arrays.stream(values())
                .filter(type -> type.jsonValue.equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown symbol type: " + value));
    }
}
