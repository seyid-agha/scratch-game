package models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TotalRewardCalculator {
    private final GameConfiguration config;

    public TotalRewardCalculator(GameConfiguration config) {
        this.config = config;
    }

    // Main method to calculate the final reward
    public double calculateReward(String[][] matrix, int betAmount) {
        Map<String, Integer> symbolCounts = countSymbols(matrix);
        double totalReward = 0;
        double flatBonus = 0;
        double bonusMultiplier = 1.0; // Default multiplier is neutral (1)

        // Process each symbol
        for (Map.Entry<String, Integer> entry : symbolCounts.entrySet()) {
            String symbol = entry.getKey();
            int count = entry.getValue();

            Symbol symbolData = config.getSymbols().get(symbol);

            if (symbolData != null) {
                String type = symbolData.getType();
                String impact = symbolData.getImpact();

                // ✅ 2) Process Standard Symbols
                if ("standard".equals(type)) {
                    double symbolReward = calculateStandardSymbolReward(matrix, symbol, count, betAmount);
                   // System.out.println("symbol reward = " + symbol + " " + symbolReward + " count = " + count);
                    totalReward += symbolReward;

                    // ✅ 4) Add Flat Bonuses
                } else if ("extra_bonus".equals(impact) && symbolData.getExtra() != null) {
                    flatBonus += symbolData.getExtra();

                    // ✅ 5) Apply Bonus Multipliers
                } else if ("multiply_reward".equals(impact) && symbolData.getRewardMultiplier() != null) {
                    //bonusMultiplier *= symbolData.getRewardMultiplier();
                        int sameBonusCount = symbolCounts.get(symbol);
                        while(sameBonusCount>0){
                            bonusMultiplier *= symbolData.getRewardMultiplier();
                            sameBonusCount--;
                        }
                }
            }
        }

        // ✅ 3) Sum total rewards and apply bonuses
        totalReward = (totalReward * bonusMultiplier) + flatBonus;
        return totalReward;
    }

    // 1) Count all unique symbols in the matrix
    private Map<String, Integer> countSymbols(String[][] matrix) {
        Map<String, Integer> symbolCounts = new HashMap<>();
        for (String[] row : matrix) {
            for (String symbol : row) {
                if (symbol != null) {
                    symbolCounts.put(symbol, symbolCounts.getOrDefault(symbol, 0) + 1);
                }
            }
        }
        return symbolCounts;
    }

    // 2) Calculate the reward for standard symbols based on win combinations
    private double calculateStandardSymbolReward(String[][] matrix, String symbol, int count, int betAmount) {
        double reward = 0;

        // Get symbol multiplier
        Symbol symbolData = config.getSymbols().get(symbol);
        double symbolMultiplier = symbolData.getRewardMultiplier() != null ? symbolData.getRewardMultiplier() : 1.0;

        // Check win combinations
        double sameSymbolMultiplier = getSameSymbolMultiplier(symbol, count);
        double linearSymbolMultiplier = getLinearSymbolMultiplier(matrix, symbol);
       // System.out.println(symbol + " count: " + count + " same_symbol_mult " + sameSymbolMultiplier + " linear_mult " + linearSymbolMultiplier);

        // Only calculate reward if at least one win condition is met
        if (sameSymbolMultiplier > 0 || linearSymbolMultiplier > 0) {
            reward = symbolMultiplier * betAmount;

            // Apply win multipliers (default to 1 if no multiplier found)
            reward *= (sameSymbolMultiplier > 0 ? sameSymbolMultiplier : 1);
            reward *= (linearSymbolMultiplier > 0 ? linearSymbolMultiplier : 1);
        }

        return reward;
    }

    // Get the multiplier from same_symbols win conditions
    private double getSameSymbolMultiplier(String symbol, int count) {
        double multiplier = 0;

        for (WinCombination combination : config.getWinCombinations().values()) {
            if ("same_symbols".equals(combination.getWhen()) && count >= combination.getCount()) {
                multiplier = Math.max(multiplier, combination.getRewardMultiplier());
            }
        }

        return multiplier;
    }

    // Get the multiplier from linear_symbols win conditions (horizontal, vertical, diagonal)
    private double getLinearSymbolMultiplier(String[][] matrix, String symbol) {
        double multiplier = 0;

        for (WinCombination combination : config.getWinCombinations().values()) {
            if ("linear_symbols".equals(combination.getWhen()) && checkLinearWin(matrix, combination, symbol)) {
                multiplier = Math.max(multiplier, combination.getRewardMultiplier());
            }
        }

        return multiplier;
    }

    // Check if the symbol meets any linear win condition (horizontal, vertical, diagonal)
    private boolean checkLinearWin(String[][] matrix, WinCombination combination, String targetSymbol) {
        if (combination.getCoveredAreas() != null) {
            for (List<String> area : combination.getCoveredAreas()) {
                boolean isWinningLine = true;

                for (String position : area) {
                    String[] parts = position.split(":");
                    int row = Integer.parseInt(parts[0]);
                    int col = Integer.parseInt(parts[1]);

                    if (!targetSymbol.equals(matrix[row][col])) {
                        isWinningLine = false;
                        break;
                    }
                }

                if (isWinningLine) {
                    return true; // Found at least one winning line
                }
            }
        }
        return false; // No winning line found
    }

    public boolean isGameLost(double totalReward) {
        return totalReward==0;
    }
}

