package com.resumebuilder.dto;

import java.util.Map;

public class ScoreResponse {
    private int totalScore;
    private Map<String, Integer> breakdown;
    private String feedback;

    public ScoreResponse() {}

    public ScoreResponse(int totalScore, Map<String, Integer> breakdown, String feedback) {
        this.totalScore = totalScore;
        this.breakdown = breakdown;
        this.feedback = feedback;
    }

    public int getTotalScore() { return totalScore; }
    public void setTotalScore(int totalScore) { this.totalScore = totalScore; }
    public Map<String, Integer> getBreakdown() { return breakdown; }
    public void setBreakdown(Map<String, Integer> breakdown) { this.breakdown = breakdown; }
    public String getFeedback() { return feedback; }
    public void setFeedback(String feedback) { this.feedback = feedback; }
}
