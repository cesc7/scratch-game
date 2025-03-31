package com.scratch.game.v1.config.model.symbol.probabilities;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Probability Configuration.
 */
public class ProbabilityConfig {
    @JsonProperty("standard_symbols")
    private List<StandardSymbolProbability> standardSymbols;
    @JsonProperty("bonus_symbols")
    private BonusSymbolProbability bonusSymbols;

    public List<StandardSymbolProbability> getStandardSymbols() {
        return standardSymbols;
    }

    public BonusSymbolProbability getBonusSymbols() {
        return bonusSymbols;
    }
}
