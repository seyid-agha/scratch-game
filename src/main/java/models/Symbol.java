package models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Symbol {
    private String type;              // "standard" or "bonus"
    @JsonProperty("reward_multiplier")
    private Double rewardMultiplier;  // Nullable for non-standard symbols
    @JsonProperty("extra")
    private Integer extra;            // Nullable for extra bonus symbols
    @JsonProperty("impact")
    private String impact;            // Nullable (only for bonus symbols)

    // Getters and Setters

    public Symbol() {}

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Double getRewardMultiplier() { return rewardMultiplier; }
    public void setRewardMultiplier(Double rewardMultiplier) { this.rewardMultiplier = rewardMultiplier; }

    public Integer getExtra() { return extra; }
    public void setExtra(Integer extra) { this.extra = extra; }

    public String getImpact() { return impact; }
    public void setImpact(String impact) { this.impact = impact; }

    @Override
    public String toString() {
        return "Type: " + type + " Reward Multiplier: " + rewardMultiplier + " Extra: " + extra + " Impact: " + impact;
    }
}
