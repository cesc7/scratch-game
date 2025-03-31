package com.scratch.game.v1.processor;


import static com.scratch.game.v1.config.model.symbol.type.ImpactType.EXTRA_BONUS;
import static org.junit.jupiter.api.Assertions.*;

import com.scratch.game.v1.response.GameResultResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.scratch.game.v1.config.GameConfig;
import com.scratch.game.v1.model.Matrix;

import java.util.List;

class GameProcessorTest {
    private GameProcessor gameProcessor;
    private GameConfig config;

    @BeforeEach
    void setup() throws Exception {
        gameProcessor = new GameProcessor();
        // Load the standard test configuration
        config = TestUtils.loadTestConfig("src/test/resource/testConfig.json");
    }

    @Test
    void testValidCaseWithBonus() {
        // Manually create a matrix with known winning combinations
        Matrix matrix = new Matrix(3, 3);
        matrix.setCell(0, 0, "A");
        matrix.setCell(1, 0, "A");
        matrix.setCell(2, 0, "A"); // Vertical line of 3 A's
        matrix.setCell(0, 1, "A");
        matrix.setCell(1, 1, "A"); // Total 5 A's (count-based win)
        matrix.setCell(2, 2, "+1000"); // Bonus symbol

        GameResultResponse result = gameProcessor.calculateResult(matrix, config, 100);

        // Verify reward: (100 * 5 * (2 * 2)) + 1000 = 2000 + 1000 = 3000
        assertEquals(3000, result.getReward());
        assertTrue(result.getAppliedWinningCombinations().get("A")
                .containsAll(List.of("same_symbol_5_times", "same_symbols_vertically")));
    }

    @Test
    void testEmptySymbolWeightsThrowsException() {
        GameConfig emptyConfig = TestUtils.createEmptyConfig();
        Matrix matrix = new Matrix(3, 3);

        assertThrows(IllegalStateException.class,
                () -> gameProcessor.generateMatrix(emptyConfig));
    }

    @Test
    void testNoBonusSymbol() {
        Matrix matrix = new Matrix(3, 3);
        matrix.setCell(0, 0, "A");
        matrix.setCell(1, 0, "A");
        matrix.setCell(2, 0, "A"); // Vertical line of 3 A's

        GameResultResponse result = gameProcessor.calculateResult(matrix, config, 100);

        // Reward: 100 * 5 * (1 * 2) = 10000 (no bonus)
        assertEquals(1000, result.getReward());
        assertNull(result.getAppliedBonusSymbol());
    }

    @Test
    void testBonusSymbolMultiplyReward() {
        Matrix matrix = new Matrix(3, 3);
        matrix.setCell(0, 0, "A");
        matrix.setCell(1, 0, "A");
        matrix.setCell(2, 0, "A"); // Vertical line
        matrix.setCell(0, 1, "+1000"); // Bonus symbol (modify config to use MULTIPLY_REWARD)

        // Update config to use multiply bonus
        config.getSymbols().get("+1000").setImpact(EXTRA_BONUS);

        GameResultResponse result = gameProcessor.calculateResult(matrix, config, 100);

        // Reward: (100 * 5 * 1 * 2) + 1000 = 1000 + 1000 = 2000
        assertEquals(2000, result.getReward());
    }

    @Test
    void testMaximumReward() {
        Matrix matrix = new Matrix(3, 3);
        // Fill matrix with "A" and a bonus symbol
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                matrix.setCell(i, j, "A");
            }
        }
        matrix.setCell(2, 2, "10x");

        GameResultResponse result = gameProcessor.calculateResult(matrix, config, 100);

        // Reward: (100 * 5 * 10 * 2 * 2 * 5 * 10) = 1,000,000
        assertEquals(1000000, result.getReward());
    }

    @Test
    void testLosingCase() {
        Matrix matrix = new Matrix(3, 3);
        // Fill with non-winning symbols
        matrix.setCell(0, 0, "B");
        matrix.setCell(1, 1, "A");
        matrix.setCell(2, 2, "C");

        GameResultResponse result = gameProcessor.calculateResult(matrix, config, 100);

        assertEquals(0, result.getReward());
        assertTrue(result.getAppliedWinningCombinations().isEmpty());
    }


}