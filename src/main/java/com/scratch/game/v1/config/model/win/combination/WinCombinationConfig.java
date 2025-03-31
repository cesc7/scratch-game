package com.scratch.game.v1.config.model.win.combination;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Win combination configuration.
 */
public class WinCombinationConfig {
    @JsonProperty("reward_multiplier")
    private double rewardMultiplier;
    private String when;
    private Integer count;
    private String group;
    @JsonProperty("covered_areas")
    private List<List<String>> coveredAreas;

    public double getRewardMultiplier() {
        return rewardMultiplier;
    }

    public String getWhen() {
        return when;
    }

    public Integer getCount() {
        return count;
    }

    public String getGroup() {
        return group;
    }

    public List<List<String>> getCoveredAreas() {
        return coveredAreas;
    }

    public void setCoveredAreas(List<List<String>> coveredAreas) {
        this.coveredAreas = coveredAreas;
    }

    public void setRewardMultiplier(double rewardMultiplier) {
        this.rewardMultiplier = rewardMultiplier;
    }
}
