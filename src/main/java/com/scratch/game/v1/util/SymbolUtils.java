package com.scratch.game.v1.util;

import com.scratch.game.v1.config.GameConfig;
import com.scratch.game.v1.config.model.symbol.Symbol;

import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class SymbolUtils {

    private SymbolUtils() {
        // Prevent instantiation
    }

    /**
     * Selects a random symbol based on weights.
     */
    public static String selectRandomSymbol(Map<String, Integer> symbolWeights) {
        Random random = new Random();
        int totalWeight = symbolWeights.values().stream().mapToInt(Integer::intValue).sum();
        int randomValue = random.nextInt(totalWeight); // Generate a random number in [0, totalWeight)

        int cumulativeWeight = 0;
        for (Map.Entry<String, Integer> entry : symbolWeights.entrySet()) {
            cumulativeWeight += entry.getValue();
            if (randomValue < cumulativeWeight) {
                return entry.getKey(); // Return the symbol corresponding to the random value
            }
        }
        throw new IllegalStateException("No symbol selected");
    }

    /**
     * Retrieves the key (symbol name) for a given Symbol object from the game configuration.
     * Returns null if not fonud.
     */
    public static String getNameFromSymbol(Symbol symbol, GameConfig config) {
        for (Map.Entry<String, Symbol> entry : config.getSymbols().entrySet()) {
            if (Objects.equals(entry.getValue(), symbol)) {
                return entry.getKey();
            }
        }
        return null;
    }
}
