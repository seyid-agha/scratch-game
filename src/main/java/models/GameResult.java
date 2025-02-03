package models;

import java.util.List;
import java.util.Map;

public class GameResult {
    private String[][] matrix;
    private double reward;
    private Map<String, List<String>> appliedWinningCombinations;
    private List<String> appliedBonusSymbols;

    public GameResult(String[][] matrix, double reward, Map<String, List<String>> appliedWinningCombinations, List<String> appliedBonusSymbols) {
        this.matrix = matrix;
        this.reward = reward;
        this.appliedWinningCombinations = appliedWinningCombinations;
        this.appliedBonusSymbols = appliedBonusSymbols;
    }

    // Getters for JSON serialization
    public String[][] getMatrix() {
        return matrix;
    }

    public double getReward() {
        return reward;
    }

    public Map<String, List<String>> getAppliedWinningCombinations() {
        return appliedWinningCombinations;
    }

    public List<String> getAppliedBonusSymbols() {
        return appliedBonusSymbols;
    }
}
