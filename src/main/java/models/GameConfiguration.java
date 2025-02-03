package models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

public class GameConfiguration {
    private int columns;
    private int rows;
    private Map<String, Symbol> symbols;
    private Probabilities probabilities;
    @JsonProperty("win_combinations")
    private Map<String, WinCombination> winCombinations;

    public int getColumns() { return columns; }
    public void setColumns(int columns) { this.columns = columns; }

    public int getRows() { return rows; }
    public void setRows(int rows) { this.rows = rows; }

    public Map<String, Symbol> getSymbols() { return symbols; }
    public void setSymbols(Map<String, Symbol> symbols) { this.symbols = symbols; }

    public Probabilities getProbabilities() { return probabilities; }
    public void setProbabilities(Probabilities probabilities) { this.probabilities = probabilities; }

    public Map<String, WinCombination> getWinCombinations() { return winCombinations; }
    public void setWinCombinations(Map<String, WinCombination> winCombinations) { this.winCombinations = winCombinations; }


    public void printConfig() {
        System.out.println("=== Game Configuration ===");
        System.out.println("Columns: " + columns);
        System.out.println("Rows: " + rows);

        // Print Symbols
        System.out.println("\n--- Symbols ---");
        symbols.forEach((key, symbol) -> {
            System.out.println("Symbol: " + key);
            System.out.println("  Type: " + symbol.getType());
            if (symbol.getRewardMultiplier() != null) {
                System.out.println("  Reward Multiplier: " + symbol.getRewardMultiplier());
            }
            if (symbol.getExtra() != null) {
                System.out.println("  Extra: " + symbol.getExtra());
            }
            if (symbol.getImpact() != null) {
                System.out.println("  Impact: " + symbol.getImpact());
            }
        });

        // Print Probabilities
        System.out.println("\n--- Probabilities ---");
        System.out.println("Standard Symbols:");
        probabilities.getStandardSymbols().forEach(entry -> {
            System.out.println("  Position: (" + entry.getColumn() + ", " + entry.getRow() + ")");
            entry.getSymbols().forEach((symbol, probability) -> {
                System.out.println("    " + symbol + ": " + probability);
            });
        });

        System.out.println("Bonus Symbols:");
        probabilities.getBonusSymbols().getSymbols().forEach((symbol, probability) -> {
            System.out.println("  " + symbol + ": " + probability);
        });

        // Print Win Combinations
        System.out.println("\n--- Win Combinations ---");
        winCombinations.forEach((key, combination) -> {
            System.out.println("Combination: " + key);
            System.out.println("  Reward Multiplier: " + combination.getRewardMultiplier());
            System.out.println("  When: " + combination.getWhen());
            System.out.println("  Group: " + combination.getGroup());
            if (combination.getCount() != null) {
                System.out.println("  Count: " + combination.getCount());
            }
            if (combination.getCoveredAreas() != null) {
                System.out.println("  Covered Areas:");
                combination.getCoveredAreas().forEach(area -> {
                    System.out.println("    " + String.join(", ", area));
                });
            }
        });

        System.out.println("===========================\n");
    }

}
