package com.scratch.game.v1.processor;

import com.scratch.game.v1.config.GameConfig;
import com.scratch.game.v1.config.model.symbol.Symbol;
import com.scratch.game.v1.config.model.symbol.type.SymbolType;
import com.scratch.game.v1.config.model.symbol.probabilities.StandardSymbolProbability;
import com.scratch.game.v1.model.Matrix;
import com.scratch.game.v1.model.WinCombinationInfo;
import com.scratch.game.v1.response.GameResultResponse;

import java.util.*;

import static com.scratch.game.v1.config.model.symbol.type.ImpactType.EXTRA_BONUS;
import static com.scratch.game.v1.config.model.symbol.type.ImpactType.MULTIPLY_REWARD;
import static com.scratch.game.v1.processor.WinCombinationProcessor.getWinCombinations;
import static com.scratch.game.v1.config.validator.GameConfigValidator.validate;
import static com.scratch.game.v1.util.MatrixUtils.countStandardSymbols;
import static com.scratch.game.v1.util.SymbolUtils.getNameFromSymbol;
import static com.scratch.game.v1.util.SymbolUtils.selectRandomSymbol;

/**
 * Processes game logic for a scratch card game, including matrix generation and reward calculation.
 */
public class GameProcessor {

    /**
     * Generates a game matrix with symbols based on the provided configuration.
     *
     * @param config The game configuration containing symbol probabilities and matrix dimensions.
     * @return A Matrix object representing the generated game state.
     */
    public Matrix generateMatrix(GameConfig config) {
        List<String> errors = validate(config);
        if (!errors.isEmpty()) {
            throw new IllegalStateException("Config not valid! errors[" + errors + "]");
        }

        int cols = config.getColumns();
        int rows = config.getRows();
        Matrix matrix = new Matrix(rows, cols);
        Random random = new Random();

        // Process standard symbols
        for (StandardSymbolProbability probability : config.getProbabilities().getStandardSymbols()) {
            int col = probability.getColumn();
            int row = probability.getRow();
            matrix.setCell(row, col, selectRandomSymbol(probability.getSymbols()));
        }

        // Process bonus symbols (single bonus symbol for the game)
        String bonusSymbol = selectRandomSymbol(config.getProbabilities().getBonusSymbols().getSymbols());

        // Place bonus symbol in random position with 25% probability
        int bonusRow = random.nextInt(rows);
        int bonusCol = random.nextInt(cols);
        if (random.nextDouble() < 0.25) {
            matrix.setCell(bonusRow, bonusCol, bonusSymbol);
        }

        return matrix;
    }

    /**
     * Calculates the game result based on the generated matrix and game configuration.
     *
     * @param matrix    The game matrix to evaluate.
     * @param config    The game configuration containing winning combinations and symbol information.
     * @param betAmount The amount bet by the player.
     * @return A GameResultResponse object containing the game outcome and applied winning combinations.
     */
    public GameResultResponse calculateResult(Matrix matrix, GameConfig config, int betAmount) {
        GameResultResponse result = new GameResultResponse();
        result.setMatrix(matrix);

        Map<String, Integer> symbolCounts = countStandardSymbols(matrix, config);
        Map<String, Map<String, WinCombinationInfo>> winCombinationSymbolGroups = getWinCombinations(matrix, config, symbolCounts);

        Map<String, List<String>> appliedCombinations = new HashMap<>();
        double totalReward = calculateSymbolRewards(config, winCombinationSymbolGroups, appliedCombinations, betAmount);

        setRewardAfterApplyingBonus(matrix, config, winCombinationSymbolGroups, result, totalReward);
        result.setAppliedWinningCombinations(appliedCombinations);
        return result;
    }


    /**
     * Calculates the total reward based on winning combinations and symbol values.
     */
    private double calculateSymbolRewards(GameConfig config,
                                          Map<String, Map<String, WinCombinationInfo>> winSymbolGroups,
                                          Map<String, List<String>> appliedCombinations, int betAmount) {
        double totalReward = 0;

        // For each symbol that has winning combinations
        for (Map.Entry<String, Map<String, WinCombinationInfo>> symbolEntry : winSymbolGroups.entrySet()) {
            String symbol = symbolEntry.getKey();
            Map<String, WinCombinationInfo> combinations = symbolEntry.getValue();

            // Get the symbol's base value from config
            Symbol symbolConfig = config.getSymbols().get(symbol);
            double symbolValue = symbolConfig.getRewardMultiplier();

            // Track combination names and calculate total multiplier
            List<String> combinationList = new ArrayList<>();
            double groupMultiplier = 1;

            // Multiply all combination multipliers together
            for (WinCombinationInfo combinationInfo : combinations.values()) {
                combinationList.add(combinationInfo.getName());
                groupMultiplier *= combinationInfo.getMultiplier();
            }

            // Calculate reward for this symbol: betAmount * base value * all multipliers
            double symbolReward = betAmount * symbolValue * groupMultiplier;
            totalReward += symbolReward;

            // Record which combinations were used
            appliedCombinations.put(symbol, combinationList);
        }

        return totalReward;
    }


    /**
     * Applies any bonus symbols to the final reward calculation.
     */
    private void setRewardAfterApplyingBonus(Matrix matrix, GameConfig config,
                                             Map<String, Map<String, WinCombinationInfo>> winSymbolGroups,
                                             GameResultResponse result, double totalReward) {
        // If no winning symbols exist, return
        if (winSymbolGroups.isEmpty()) return;

        double bonusReward = 0;
        String appliedBonusSymbol = null;

        for (String[] row : matrix.getCells()) {
            for (String cell : row) {
                Symbol symbolConfig = config.getSymbols().get(cell);

                // Check if the current cell contains a bonus symbol
                if (symbolConfig != null && symbolConfig.getType() == SymbolType.BONUS) {
                    appliedBonusSymbol = getNameFromSymbol(symbolConfig, config);
                    result.setAppliedBonusSymbol(appliedBonusSymbol);

                    // Apply the bonus based on its impact type
                    if (symbolConfig.getImpact() == EXTRA_BONUS) {
                        bonusReward = symbolConfig.getExtra(); // Add extra reward
                    } else if (symbolConfig.getImpact() == MULTIPLY_REWARD) {
                        bonusReward = totalReward * symbolConfig.getRewardMultiplier() - totalReward;
                    }
                    break;
                }
            }
            if (appliedBonusSymbol != null) break;
        }
        result.setReward(totalReward + bonusReward);
    }
}
