package com.scratch.game.v1.config.model.symbol.probabilities;

import java.util.Map;

/**
 * StandardSymbolProbability configuration.
 */
public class StandardSymbolProbability {
    private int column;
    private int row;
    private Map<String, Integer> symbols;

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }

    public Map<String, Integer> getSymbols() {
        return symbols;
    }
}