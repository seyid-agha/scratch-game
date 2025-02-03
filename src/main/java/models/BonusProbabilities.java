package models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class BonusProbabilities {
    @JsonProperty("symbols")
    private Map<String, Integer> symbols; // Symbol name -> Probability value

    // Default constructor
    public BonusProbabilities() {}

    // Getters and Setters
    public Map<String, Integer> getSymbols() { return symbols; }
    public void setSymbols(Map<String, Integer> symbols) { this.symbols = symbols; }
}
