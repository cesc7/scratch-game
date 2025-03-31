package com.scratch.game.v1.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.scratch.game.v1.config.model.symbol.probabilities.ProbabilityConfig;
import com.scratch.game.v1.config.model.symbol.Symbol;
import com.scratch.game.v1.config.model.win.combination.WinCombinationConfig;

import java.util.Map;


/**
 * Represents the configuration for the scratch card game.
 * This class contains all the necessary configurations such as:
 * - Matrix dimensions (rows and columns)
 * - Symbol definitions and probabilities
 * - Winning combinations and their rules
 */
public class GameConfig {
    private int columns = 3;
    private int rows = 3;
    private Map<String, Symbol> symbols;
    private ProbabilityConfig probabilities;
    @JsonProperty("win_combinations")
    private Map<String, WinCombinationConfig> winCombinations;

    public int getColumns() {
        return columns;
    }

    public int getRows() {
        return rows;
    }

    public Map<String, Symbol> getSymbols() {
        return symbols;
    }

    public ProbabilityConfig getProbabilities() {
        return probabilities;
    }

    public Map<String, WinCombinationConfig> getWinCombinations() {
        return winCombinations;
    }

    public void setSymbols(Map<String, Symbol> symbols) {
        this.symbols = symbols;
    }
}