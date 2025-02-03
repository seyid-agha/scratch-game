import com.fasterxml.jackson.databind.ObjectMapper;
import models.*;

import java.io.IOException;

public class Application {

    public static void main(String[] args) {
        String configPath = null;
        int bettingAmount = 0;

        // Parse command-line arguments
        for (int i = 0; i < args.length; i++) {
            if ("--config".equals(args[i]) && i + 1 < args.length) {
                configPath = args[i + 1];
            } else if ("--betting-amount".equals(args[i]) && i + 1 < args.length) {
                bettingAmount = Integer.parseInt(args[i + 1]);
            }
        }

        if (configPath == null) {
            System.err.println("Usage: java -jar game.jar --config <config.json> --betting-amount <amount>");
            System.exit(1);
        }

        try {
            GameConfiguration config = ConfigParser.parseConfig(configPath);
            //System.out.println("Game Loaded: " + config);
            //config.printConfig();
            MatrixGenerator generator = new MatrixGenerator(config);
//
//            WinChecker winChecker = new WinChecker(config);
            GameResultCalculator calculator = new GameResultCalculator(config);
//
            String[][] matrix = generator.generateMatrix();
            //generator.printMatrix(matrix);

//            String[][] matrix = {{"C", "F", "C"},{"5x","F","A"}, {"C","E", "E"}};
 //           String[][] matrix1 = {{"A", "A", "B"},{"A","+1000","B"}, {"A","A", "B"}};
//            String[][] matrix2 = {{"A", "B", "C"},{"E", "B", "5x"}, {"F", "D", "C"}};
//            String[][] matrix3 = {{"A", "B", "C"},{"E", "B", "10x"}, {"F", "D", "B"}};
 //           String[][] matrix4 = {{"A", "A", "A"},{"5x", "B", "5x"}, {"C", "D", "E"}};
//            String[][] matrix5 = {{"A", "A", "A"},{"5x", "B", "5x"}, {"A", "A", "A"}};

            //double totalReward = winChecker.calculateTotalReward(matrix, bettingAmount);
            //double  totalReward = rewardCalculator.calculateReward(matrix,bettingAmount);
            // Calculate the result
            GameResult result = calculator.calculateReward(matrix, bettingAmount);
            // Convert result to JSON using Jackson
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonResult = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(result);

            // Print the JSON result
            System.out.println(jsonResult);
//
//            if (calculator.isGameLost(result.getReward())) {
//                System.out.println("You lost the game!");
//            } else {
//                System.out.println("You won! Total Reward: " + result.getReward());
//            }



        } catch (IOException e) {
            System.err.println("Failed to load config: " + e.getMessage());
        }
    }
}
