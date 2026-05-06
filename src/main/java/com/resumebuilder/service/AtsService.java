package com.resumebuilder.service;

import com.resumebuilder.dto.AtsCheckResponse;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class AtsService {

    public AtsCheckResponse checkAts(String resumeContent, String jobDescription) {
        Set<String> resumeWords = extractKeywords(resumeContent);
        Set<String> jobWords = extractKeywords(jobDescription);

        List<String> matched = new ArrayList<>();
        List<String> missing = new ArrayList<>();

        for (String keyword : jobWords) {
            if (keyword.length() > 2) {
                if (resumeWords.contains(keyword)) {
                    matched.add(keyword);
                } else {
                    missing.add(keyword);
                }
            }
        }

        // Sort by importance (longer keywords tend to be more specific/important)
        matched.sort((a, b) -> b.length() - a.length());
        missing.sort((a, b) -> b.length() - a.length());

        // Limit to top keywords
        List<String> topMatched = matched.stream().limit(20).collect(Collectors.toList());
        List<String> topMissing = missing.stream().limit(20).collect(Collectors.toList());

        int matchScore = jobWords.isEmpty() ? 0 :
                (int) ((double) matched.size() / jobWords.size() * 100);
        matchScore = Math.min(matchScore, 100);

        String recommendation = generateRecommendation(matchScore, topMissing);

        return new AtsCheckResponse(matchScore, topMatched, topMissing, recommendation);
    }

    private Set<String> extractKeywords(String text) {
        if (text == null || text.isBlank()) return new HashSet<>();

        // Remove special characters, lowercase, split into words
        String cleaned = text.toLowerCase()
                .replaceAll("[^a-z0-9\\s+#]", " ")
                .replaceAll("\\s+", " ")
                .trim();

        Set<String> words = new HashSet<>(Arrays.asList(cleaned.split(" ")));

        // Also extract multi-word phrases (bigrams)
        String[] parts = cleaned.split(" ");
        for (int i = 0; i < parts.length - 1; i++) {
            if (parts[i].length() > 2 && parts[i + 1].length() > 2) {
                words.add(parts[i] + " " + parts[i + 1]);
            }
        }

        // Remove common stop words
        Set<String> stopWords = Set.of("the", "and", "for", "are", "but", "not", "you",
                "all", "can", "her", "was", "one", "our", "out", "day", "get", "has",
                "him", "his", "how", "its", "may", "new", "now", "old", "see", "two",
                "who", "boy", "did", "she", "use", "way", "will", "with", "have", "this",
                "that", "from", "they", "been", "more", "also", "into", "than", "then",
                "when", "your", "what", "some", "time", "very", "just", "know", "take",
                "year", "good", "much", "work", "well", "such", "give", "most", "over");

        words.removeAll(stopWords);
        words.removeIf(w -> w.length() <= 2);

        return words;
    }

    private String generateRecommendation(int score, List<String> missing) {
        StringBuilder sb = new StringBuilder();

        if (score >= 80) {
            sb.append("Excellent ATS match! Your resume aligns well with the job description.");
        } else if (score >= 60) {
            sb.append("Good match. Consider adding more relevant keywords to improve your score.");
        } else if (score >= 40) {
            sb.append("Moderate match. Your resume needs more alignment with the job requirements.");
        } else {
            sb.append("Low match. Significantly update your resume to include more job-specific keywords.");
        }

        if (!missing.isEmpty()) {
            sb.append(" Consider adding these key terms: ");
            sb.append(missing.stream().limit(5).collect(Collectors.joining(", ")));
            sb.append(".");
        }

        return sb.toString();
    }
}
