package models;

import java.util.*;

public class MatrixGenerator {
    private final GameConfiguration config;
    private final Random random;

    public MatrixGenerator(GameConfiguration config) {
        this.config = config;
        this.random = new Random();
    }

    // Main method to generate the matrix
    public String[][] generateMatrix() {
        int rows = config.getRows();
        int columns = config.getColumns();
        String[][] matrix = new String[rows][columns];

        // Place standard symbols
        for (ProbabilityByCell entry : config.getProbabilities().getStandardSymbols()) {
            int col = entry.getColumn();
            int row = entry.getRow();
            matrix[row][col] = getRandomSymbol(entry.getSymbols());
        }
        // Print matrix before bonus symbols generation
//        System.out.println("Print matrix before bonus symbols generation:");
//        printMatrix(matrix);
//        System.out.println("Print matrix after bonus symbols generation:");
        // Add bonus symbols randomly (optional)
        addBonusSymbol(matrix);

        return matrix;
    }

    //  Select a symbol based on weighted probability
    private String getRandomSymbol(Map<String, Integer> probabilities) {
        int totalWeight = probabilities.values().stream().mapToInt(Integer::intValue).sum();
        int randomValue = random.nextInt(totalWeight) + 1; // 1 to totalWeight

        int cumulativeWeight = 0;
        for (Map.Entry<String, Integer> entry : probabilities.entrySet()) {
            cumulativeWeight += entry.getValue();
            if (randomValue <= cumulativeWeight) {
                return entry.getKey();
            }
        }
        return null; // Shouldn't happen
    }

    //  Randomly place bonus symbols based on their probabilities
    private void addBonusSymbol(String[][] matrix) {
        Map<String, Integer> bonusSymbols = config.getProbabilities().getBonusSymbols().getSymbols();
        int totalWeight = bonusSymbols.values().stream().mapToInt(Integer::intValue).sum();

        for (int row = 0; row < matrix.length; row++) {
            for (int col = 0; col < matrix[0].length; col++) {
                if (random.nextDouble() < 0.1) { // 10% chance to place a bonus symbol
                    String bonusSymbol = getRandomSymbol(bonusSymbols);
                    matrix[row][col] = bonusSymbol;
              //      break;  //only add 1 bonus symbol as config requires
                }
            }
        }
    }

    //  Display the matrix
    public void printMatrix(String[][] matrix) {
        System.out.println("\n=== Generated Matrix ===");
        System.out.println("rows: " + matrix.length + " cols: " + matrix[0].length );
        for (String[] row : matrix) {
            for (String symbol : row) {
                System.out.print((symbol != null ? symbol : " ") + "\t");
            }
            System.out.println();
        }
    }
}

