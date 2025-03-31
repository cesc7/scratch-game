package com.scratch.game.v1.processor;

import com.scratch.game.v1.config.GameConfig;
import com.scratch.game.v1.config.model.symbol.Symbol;
import com.scratch.game.v1.config.model.symbol.type.SymbolType;
import com.scratch.game.v1.config.model.win.combination.WinCombinationConfig;
import com.scratch.game.v1.model.Matrix;
import com.scratch.game.v1.model.WinCombinationInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WinCombinationProcessor {

    private WinCombinationProcessor() {
        // Prevent instantiation
    }

    /**
     * Identifies winning combinations in the matrix based on the game configuration.
     */
    public static Map<String, Map<String, WinCombinationInfo>> getWinCombinations(
            Matrix matrix, GameConfig config, Map<String, Integer> symbolCounts) {

        Map<String, Map<String, WinCombinationInfo>> symbolGroups = new HashMap<>();

        config.getWinCombinations().forEach((combinationName, winCombinationConfig) -> {
            if ("same_symbols".equals(winCombinationConfig.getWhen())) {
                processCountCombinations(combinationName, winCombinationConfig, symbolCounts, symbolGroups);
            } else if ("linear_symbols".equals(winCombinationConfig.getWhen())) {
                processLinearCombinations(matrix, config, combinationName, winCombinationConfig, symbolGroups);
            }
        });

        return symbolGroups;
    }

    /**
     * Processes count-based winning combinations (e.g., "3 of a kind", "5 of a kind").
     */
    private static void processCountCombinations(String winCombinationName, WinCombinationConfig winCombinationConfig,
                                                 Map<String, Integer> symbolCounts,
                                                 Map<String, Map<String, WinCombinationInfo>> symbolGroups) {
        // For each symbol and its count in the matrix
        for (Map.Entry<String, Integer> symbolEntry : symbolCounts.entrySet()) {
            String currentSymbol = symbolEntry.getKey();
            int symbolCount = symbolEntry.getValue();
            int requiredCount = winCombinationConfig.getCount();

            // Check if symbol meets the minimum count requirement
            if (symbolCount >= requiredCount) {
                String comboGroup = winCombinationConfig.getGroup();
                double comboMultiplier = winCombinationConfig.getRewardMultiplier();

                // Get or create group map for this symbol
                Map<String, WinCombinationInfo> groupMap = symbolGroups.computeIfAbsent(
                        currentSymbol,
                        k -> new HashMap<>()
                );

                // Check if there's an existing combination in this group
                WinCombinationInfo existingCombo = groupMap.get(comboGroup);

                // Keep the combination with the highest multiplier
                if (existingCombo == null || comboMultiplier > existingCombo.getMultiplier()) {
                    groupMap.put(comboGroup, new WinCombinationInfo(winCombinationName, comboMultiplier));
                }
            }
        }
    }

    /**
     * Processes linear winning combinations (e.g., "3 in a row", "diagonal").
     */
    private static void processLinearCombinations(Matrix matrix, GameConfig config,
                                                  String combinationName, WinCombinationConfig combinationConfig,
                                                  Map<String, Map<String, WinCombinationInfo>> symbolGroups) {
        // Iterate through all covered areas defined in the combination configuration
        for (List<String> area : combinationConfig.getCoveredAreas()) {

            // Get the symbol from the specified area in the matrix (Winning Symbol)
            String winingLinearSymbol = getWinningLinearSymbol(matrix, config, area);

            // If a Wining symbol is found (all positions in the area contain the same symbol)
            if (winingLinearSymbol != null) {
                // Get or create the group map for this symbol
                Map<String, WinCombinationInfo> groupMap = symbolGroups.computeIfAbsent(winingLinearSymbol, k -> new HashMap<>());

                // Check if there's an existing winning combination in this group
                WinCombinationInfo existingCombo = groupMap.get(combinationConfig.getGroup());
                double newMultiplier = combinationConfig.getRewardMultiplier();

                // Add or update the combination with the highest multiplier
                if (existingCombo == null || newMultiplier > existingCombo.getMultiplier()) {
                    groupMap.put(combinationConfig.getGroup(), new WinCombinationInfo(combinationName, newMultiplier));
                }
            }
        }
    }

    /**
     * Identifies the winning symbol in a linear combination.
     * Returns null if not found any.
     */
    public static String getWinningLinearSymbol(Matrix matrix, GameConfig config, List<String> area) {
        String symbol = null;
        for (String pos : area) {
            String[] parts = pos.split(":");
            int row = Integer.parseInt(parts[0]);
            int col = Integer.parseInt(parts[1]);
            String current = matrix.getCells()[row][col];
            Symbol sc = config.getSymbols().get(current);

            if (sc == null || sc.getType() != SymbolType.STANDARD) {
                return null;
            } else if (symbol == null) {
                symbol = current;
            } else if (!symbol.equals(current)) {
                return null;
            }
        }
        return symbol;
    }
}
