package com.scratch.game.v1.config.model.symbol;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.scratch.game.v1.config.model.symbol.type.ImpactType;
import com.scratch.game.v1.config.model.symbol.type.SymbolType;


/**
 * Symbol configuration.
 */
public class Symbol {
    @JsonProperty("reward_multiplier")
    private double rewardMultiplier = 1.0;
    private SymbolType type;
    private ImpactType impact;
    private Integer extra;

    public double getRewardMultiplier() {
        return rewardMultiplier;
    }

    public SymbolType getType() {
        return type;
    }

    public ImpactType getImpact() {
        return impact;
    }

    public Integer getExtra() {
        return extra;
    }

    public void setRewardMultiplier(double rewardMultiplier) {
        this.rewardMultiplier = rewardMultiplier;
    }

    public void setImpact(ImpactType impact) {
        this.impact = impact;
    }
}