package com.scratch.game.v1.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.scratch.game.v1.model.Matrix;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Response object.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GameResultResponse {
    private Matrix matrix;
    private double reward;
    private Map<String, List<String>> appliedWinningCombinations;
    private String appliedBonusSymbol;


    public Matrix getMatrix() {
        return matrix;
    }

    @JsonProperty("matrix")
    public List<List<String>> getMatrixAsNestedLists() {
        return Arrays.stream(matrix.getCells())
                .map(Arrays::asList)
                .collect(Collectors.toList());
    }

    public void setMatrix(Matrix matrix) {
        this.matrix = matrix;
    }

    public double getReward() {
        return reward;
    }

    public void setReward(double reward) {
        this.reward = reward;
    }

    public Map<String, List<String>> getAppliedWinningCombinations() {
        return appliedWinningCombinations;
    }

    public void setAppliedWinningCombinations(Map<String, List<String>> appliedWinningCombinations) {
        this.appliedWinningCombinations = appliedWinningCombinations;
    }

    public String getAppliedBonusSymbol() {
        return appliedBonusSymbol;
    }

    public void setAppliedBonusSymbol(String appliedBonusSymbol) {
        this.appliedBonusSymbol = appliedBonusSymbol;
    }

    /**
     * toString method overriden since json beautifier didn't print matrix 2D.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{\n");
        sb.append("    \"matrix\": [\n");
        for (String[] row : matrix.getCells()) {
            sb.append("        [");
            for (int i = 0; i < row.length; i++) {
                sb.append("\"").append(row[i]).append("\"");
                if (i < row.length - 1) {
                    sb.append(", ");
                }
            }
            sb.append("]");
            if (Arrays.asList(matrix.getCells()).indexOf(row) < matrix.getCells().length - 1) {
                sb.append(",");
            }
            sb.append("\n");
        }
        sb.append("    ],\n");
        sb.append("    \"reward\": ").append(reward).append(",\n");
        if (appliedWinningCombinations != null && !appliedWinningCombinations.isEmpty()) {
            sb.append("    \"applied_winning_combinations\": {\n");
            appliedWinningCombinations.forEach((symbol, combos) -> {
                sb.append("        \"").append(symbol).append("\": [");
                for (int i = 0; i < combos.size(); i++) {
                    sb.append("\"").append(combos.get(i)).append("\"");
                    if (i < combos.size() - 1) {
                        sb.append(", ");
                    }
                }
                sb.append("]\n");
            });
            sb.append("    },\n");
        }
        if (appliedBonusSymbol != null) {
            sb.append("    \"applied_bonus_symbol\": \"").append(appliedBonusSymbol).append("\"\n");
        }
        sb.append("}");
        return sb.toString();
    }
}