package models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GameResultCalculator {


        private final GameConfiguration config;

        // To track applied winning combinations and bonuses
        private Map<String, List<String>> appliedWinningCombinations;
        private List<String> appliedBonusSymbols;

        public GameResultCalculator(GameConfiguration config) {
            this.config = config;
        }


    // Method to return GameResult object
        public GameResult calculateReward(String[][] matrix, int betAmount) {
            appliedWinningCombinations = new HashMap<>();
            appliedBonusSymbols = new ArrayList<>();

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
                    //System.out.println(" symbol " + symbol + " " + symbolData.toString() + " item count " + symbolCounts.get(symbol));

                    // Process Standard Symbols
                    if ("standard".equals(type)) {
                        double symbolReward = calculateStandardSymbolReward(matrix, symbol, count, betAmount);
                        totalReward += symbolReward;

                        // Add Flat Bonuses
                    } else if ("extra_bonus".equals(impact) && symbolData.getExtra() != null) {
                        flatBonus += symbolData.getExtra();
                        appliedBonusSymbols.add(symbol);  // Track flat bonus symbols

                        // Apply Bonus Multipliers
                    } else if ("multiply_reward".equals(impact) && symbolData.getRewardMultiplier() != null) {
                        int sameBonusCount = symbolCounts.get(symbol);
                        while(sameBonusCount>0){
                            bonusMultiplier *= symbolData.getRewardMultiplier();
                            appliedBonusSymbols.add(symbol);
                            sameBonusCount--;
                        }
                          // Track multiplier bonus symbols
                    }
                }
            }

            // Final reward calculation
            totalReward = (totalReward * bonusMultiplier) + flatBonus;

            // Return the structured game result
            return new GameResult(matrix, totalReward, appliedWinningCombinations, appliedBonusSymbols);
        }

        // Count symbols in the matrix
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

        // Calculate rewards for standard symbols and track winning combinations
        private double calculateStandardSymbolReward(String[][] matrix, String symbol, int count, int betAmount) {
            double reward = 0;
            Symbol symbolData = config.getSymbols().get(symbol);
            double symbolMultiplier = symbolData.getRewardMultiplier() != null ? symbolData.getRewardMultiplier() : 1.0;

            List<String> wins = new ArrayList<>(); // Track applied wins for this symbol

            // Check win combinations
            double sameSymbolMultiplier = getSameSymbolMultiplier(symbol, count, wins);
            double linearSymbolMultiplier = getLinearSymbolMultiplier(matrix, symbol, wins);

            if (sameSymbolMultiplier > 0 || linearSymbolMultiplier > 0) {
                reward = symbolMultiplier * betAmount;
                reward *= (sameSymbolMultiplier > 0 ? sameSymbolMultiplier : 1);
                reward *= (linearSymbolMultiplier > 0 ? linearSymbolMultiplier : 1);
            }

            // Store the applied winning combinations for the symbol
            if (!wins.isEmpty()) {
               // System.out.println(symbol + " count= " + count + " " + wins);
                appliedWinningCombinations.put(symbol, wins);
            }

            return reward;
        }

        // Get same_symbol multipliers and track the win combination names
        private double getSameSymbolMultiplier(String symbol, int count, List<String> wins) {
            double maxMultiplier = 0;
            String maxWinCombination = null;

            for (Map.Entry<String, WinCombination> entry : config.getWinCombinations().entrySet()) {
                WinCombination combination = entry.getValue();

                if ("same_symbols".equals(combination.getWhen()) && count >= combination.getCount()) {
                    if (combination.getRewardMultiplier() > maxMultiplier) {
                        maxMultiplier = combination.getRewardMultiplier();
                        maxWinCombination = entry.getKey();  // Track only the highest win combination
                    }
                }
            }

            if (maxWinCombination != null) {
                wins.add(maxWinCombination);  // Add only the highest win combination
            }

            return maxMultiplier;
        }

        // Get linear_symbol multipliers and track the win combination names
        private double getLinearSymbolMultiplier(String[][] matrix, String symbol, List<String> wins) {
            double multiplier = 0;

            for (Map.Entry<String, WinCombination> entry : config.getWinCombinations().entrySet()) {
                WinCombination combination = entry.getValue();
                if ("linear_symbols".equals(combination.getWhen()) && checkLinearWin(matrix, combination, symbol)) {
                    multiplier = Math.max(multiplier, combination.getRewardMultiplier());
                    wins.add(entry.getKey());  // Track the applied win
                }
            }

            return multiplier;
        }

        // Check if a symbol meets any linear win condition (e.g., horizontal, vertical)
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
                        return true;
                    }
                }
            }
            return false;
        }


}


