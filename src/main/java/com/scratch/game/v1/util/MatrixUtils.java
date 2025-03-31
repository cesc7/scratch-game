package com.scratch.game.v1.util;

import com.scratch.game.v1.config.GameConfig;
import com.scratch.game.v1.config.model.symbol.Symbol;
import com.scratch.game.v1.config.model.symbol.type.SymbolType;
import com.scratch.game.v1.model.Matrix;

import java.util.HashMap;
import java.util.Map;

public class MatrixUtils {

    private MatrixUtils() {
        // Prevent instantiation
    }

    /**
     * Counts occurrences of standard symbols in the matrix.
     */
    public static Map<String, Integer> countStandardSymbols(Matrix matrix, GameConfig config) {
        Map<String, Integer> counts = new HashMap<>();
        for (String[] row : matrix.getCells()) {
            for (String symbol : row) {
                Symbol sc = config.getSymbols().get(symbol);
                if (sc != null && sc.getType() == SymbolType.STANDARD) {
                    counts.put(symbol, counts.getOrDefault(symbol, 0) + 1);
                }
            }
        }
        return counts;
    }
}
