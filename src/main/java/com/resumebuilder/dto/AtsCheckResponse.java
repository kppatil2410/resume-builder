package com.resumebuilder.dto;

import java.util.List;

public class AtsCheckResponse {
    private int matchScore;
    private List<String> matchedKeywords;
    private List<String> missingKeywords;
    private String recommendation;

    public AtsCheckResponse() {}

    public AtsCheckResponse(int matchScore, List<String> matchedKeywords,
                            List<String> missingKeywords, String recommendation) {
        this.matchScore = matchScore;
        this.matchedKeywords = matchedKeywords;
        this.missingKeywords = missingKeywords;
        this.recommendation = recommendation;
    }

    public int getMatchScore() { return matchScore; }
    public void setMatchScore(int matchScore) { this.matchScore = matchScore; }
    public List<String> getMatchedKeywords() { return matchedKeywords; }
    public void setMatchedKeywords(List<String> matchedKeywords) { this.matchedKeywords = matchedKeywords; }
    public List<String> getMissingKeywords() { return missingKeywords; }
    public void setMissingKeywords(List<String> missingKeywords) { this.missingKeywords = missingKeywords; }
    public String getRecommendation() { return recommendation; }
    public void setRecommendation(String recommendation) { this.recommendation = recommendation; }
}
