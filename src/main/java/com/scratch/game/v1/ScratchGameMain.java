package com.scratch.game.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.scratch.game.v1.config.GameConfig;
import com.scratch.game.v1.processor.GameProcessor;
import com.scratch.game.v1.response.GameResultResponse;
import com.scratch.game.v1.model.Matrix;

import java.io.IOException;

public class ScratchGameMain {
    private static int betAmount;
    private static final GameProcessor processor = new GameProcessor();

    public static void main(String[] args) throws Exception {
        if (invalid(args)) return;

        try {
            GameConfig config = parseGameConfigFromInput(args);

            Matrix matrix = processor.generateMatrix(config);
            GameResultResponse result = processor.calculateResult(matrix, config, betAmount);

            System.out.println(result);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }

    }

    private static boolean invalid(String[] args) {
        if (args.length < 4 || !args[0].equals("--config") || !args[2].equals("--betting-amount")) {
            System.err.println("Usage: java -jar scratch-game.jar --config <config-file> --betting-amount <amount>");
            return true;
        }
        return false;
    }

    private static GameConfig parseGameConfigFromInput(String[] args) throws IOException {
        String configPath = args[1];
        betAmount = Integer.parseInt(args[3]);

        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        return mapper.readValue(ClassLoader.getSystemResource(configPath), GameConfig.class);
    }
}
