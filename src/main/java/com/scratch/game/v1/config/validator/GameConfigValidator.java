package com.scratch.game.v1.config.validator;

import com.scratch.game.v1.config.GameConfig;
import com.scratch.game.v1.config.model.symbol.Symbol;
import com.scratch.game.v1.config.model.symbol.probabilities.ProbabilityConfig;
import com.scratch.game.v1.config.model.win.combination.WinCombinationConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GameConfigValidator {

    private GameConfigValidator() {
        // Prevent instantiation
    }

    /**
     * Validates the game configuration for matrix generation.
     * Returns a list of error messages if invalid, empty list if valid.
     */
    public static List<String> validate(GameConfig config) {
        List<String> errors = new ArrayList<>();
        int rows = config.getRows();
        int columns = config.getColumns();

        // 1. Validate matrix dimensions
        if (rows <= 0 || columns <= 0) {
            errors.add("Invalid matrix dimensions: rows and columns must be positive integers");
        }

        // 2. Validate symbols configuration
        Map<String, Symbol> symbols = config.getSymbols();
        if (symbols == null || symbols.isEmpty()) {
            errors.add("No symbols defined in configuration");
        } else {
            symbols.forEach((name, symbol) -> {
                if (symbol.getRewardMultiplier() < 0) {
                    errors.add("Symbol " + name + " has invalid reward multiplier");
                }
            });
        }

        // 3. Validate probabilities
        ProbabilityConfig probabilities = config.getProbabilities();
        if (probabilities == null) {
            errors.add("Missing probability configuration");
        } else {
            // Validate standard symbols coverage
            if (probabilities.getStandardSymbols().size() != rows * columns) {
                errors.add("Standard symbol probabilities don't match matrix dimensions");
            }

            // Validate bonus symbol probabilities
            if (probabilities.getBonusSymbols() != null &&
                    probabilities.getBonusSymbols().getSymbols().values().stream()
                            .anyMatch(w -> w < 0)) {
                errors.add("Bonus symbols contain negative weights");
            }
        }

        // 4. Validate win combinations
        Map<String, WinCombinationConfig> winCombinations = config.getWinCombinations();
        if (winCombinations == null || winCombinations.isEmpty()) {
            errors.add("No winning combinations defined");
        } else {
            winCombinations.forEach((name, combo) -> {
                if (combo.getRewardMultiplier() <= 0) {
                    errors.add("Win combination " + name + " has invalid multiplier");
                }

                // Validate combination-specific rules
                if (combo.getWhen().equals("same_symbols")) {
                    if (combo.getCount() <= 0) {
                        errors.add("Win combination " + name + " has invalid count");
                    }
                } else if (combo.getWhen().equals("linear_symbols")) {
                    if (combo.getCoveredAreas() == null || combo.getCoveredAreas().isEmpty()) {
                        errors.add("Win combination " + name + " missing covered areas");
                    }
                }
            });
        }

        return errors;
    }
}
