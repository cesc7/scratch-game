package com.scratch.game.v1.model;

/**
 * Class repreenting win combination information. Name and multiplier.
 */
public class WinCombinationInfo {
    final String name;
    final double multiplier;

    public WinCombinationInfo(String name, double multiplier) {
        this.name = name;
        this.multiplier = multiplier;
    }

    public String getName() {
        return name;
    }

    public double getMultiplier() {
        return multiplier;
    }
}
