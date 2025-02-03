package models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class WinCombination {
    @JsonProperty("reward_multiplier")
    private double rewardMultiplier;

    private String when;
    private String group;
    private Integer count; // Nullable (only present for same_symbol_X_times)

    @JsonProperty("covered_areas")
    private List<List<String>> coveredAreas; // Nullable (only present for horizontal/vertical wins)

    // Default constructor required by Jackson
    public WinCombination() {}

    // Getters and Setters
    public double getRewardMultiplier() { return rewardMultiplier; }
    public void setRewardMultiplier(double rewardMultiplier) { this.rewardMultiplier = rewardMultiplier; }

    public String getWhen() { return when; }
    public void setWhen(String when) { this.when = when; }

    public String getGroup() { return group; }
    public void setGroup(String group) { this.group = group; }

    public Integer getCount() { return count; }
    public void setCount(Integer count) { this.count = count; }

    public List<List<String>> getCoveredAreas() { return coveredAreas; }
    public void setCoveredAreas(List<List<String>> coveredAreas) { this.coveredAreas = coveredAreas; }
}
