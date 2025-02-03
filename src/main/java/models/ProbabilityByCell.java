package models;

import java.util.Map;

public class ProbabilityByCell {
    private int column;
    private int row;
    private Map<String, Integer> symbols; // Symbol name -> Probability value

    // Default constructor required by Jackson
    public ProbabilityByCell() {}

    // Getters and Setters
    public int getColumn() { return column; }
    public void setColumn(int column) { this.column = column; }

    public int getRow() { return row; }
    public void setRow(int row) { this.row = row; }

    public Map<String, Integer> getSymbols() { return symbols; }
    public void setSymbols(Map<String, Integer> symbols) { this.symbols = symbols; }
}
