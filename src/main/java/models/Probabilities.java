package models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Probabilities {
    @JsonProperty("standard_symbols")
    private List<ProbabilityByCell> standardSymbols; // List of (column, row) probabilities

    @JsonProperty("bonus_symbols")
    private BonusProbabilities bonusSymbols; // Bonus symbols probability mapping

    // Default constructor
    public Probabilities() {}

    // Getters and Setters
    public List<ProbabilityByCell> getStandardSymbols() { return standardSymbols; }
    public void setStandardSymbols(List<ProbabilityByCell> standardSymbols) { this.standardSymbols = standardSymbols; }

    public BonusProbabilities getBonusSymbols() { return bonusSymbols; }
    public void setBonusSymbols(BonusProbabilities bonusSymbols) { this.bonusSymbols = bonusSymbols; }
}
