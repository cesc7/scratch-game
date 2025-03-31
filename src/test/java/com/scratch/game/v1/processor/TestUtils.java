package com.scratch.game.v1.processor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scratch.game.v1.config.GameConfig;

import java.io.File;
import java.util.List;
import java.util.Map;

class TestUtils {
    static GameConfig loadTestConfig(String path) throws Exception {
        return new ObjectMapper().readValue(new File(path), GameConfig.class);
    }

    static GameConfig createEmptyConfig() {
        GameConfig config = new GameConfig();
        config.setSymbols(Map.of()); // Empty symbols
        return config;
    }

    static GameConfig modifyConfigWithInvalidArea(GameConfig original) {
        original.getWinCombinations().get("same_symbols_vertically")
                .setCoveredAreas(List.of(List.of("3:3"))); // Invalid position
        return original;
    }
}
