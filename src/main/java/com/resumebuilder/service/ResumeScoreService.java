package com.resumebuilder.service;

import com.resumebuilder.dto.ScoreResponse;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ResumeScoreService {

    private static final List<String> POWER_WORDS = Arrays.asList(
            "achieved", "improved", "led", "managed", "developed", "created", "implemented",
            "designed", "built", "launched", "increased", "reduced", "optimized", "delivered",
            "collaborated", "coordinated", "spearheaded", "streamlined", "transformed", "drove"
    );

    private static final List<String> TECH_KEYWORDS = Arrays.asList(
            "java", "python", "javascript", "react", "spring", "sql", "aws", "docker",
            "kubernetes", "git", "agile", "scrum", "api", "rest", "microservices", "ci/cd",
            "machine learning", "data", "cloud", "devops", "html", "css", "node", "angular"
    );

    private static final List<String> SECTION_KEYWORDS = Arrays.asList(
            "experience", "education", "skills", "projects", "summary", "objective",
            "certifications", "achievements", "work history", "professional"
    );

    public ScoreResponse calculateScore(String resumeContent) {
        if (resumeContent == null || resumeContent.isBlank()) {
            return new ScoreResponse(0, new HashMap<>(), "Resume content is empty.");
        }

        String lower = resumeContent.toLowerCase();
        Map<String, Integer> breakdown = new LinkedHashMap<>();

        // 1. Keywords score (25 points)
        int keywordScore = scoreKeywords(lower);
        breakdown.put("Keywords & Action Verbs", keywordScore);

        // 2. Structure score (25 points)
        int structureScore = scoreStructure(lower);
        breakdown.put("Structure & Sections", structureScore);

        // 3. Length score (20 points)
        int lengthScore = scoreLength(resumeContent);
        breakdown.put("Content Length", lengthScore);

        // 4. Skills score (20 points)
        int skillsScore = scoreTechSkills(lower);
        breakdown.put("Technical Skills", skillsScore);

        // 5. Contact info score (10 points)
        int contactScore = scoreContactInfo(lower);
        breakdown.put("Contact Information", contactScore);

        int total = keywordScore + structureScore + lengthScore + skillsScore + contactScore;
        String feedback = generateFeedback(total, breakdown);

        return new ScoreResponse(total, breakdown, feedback);
    }

    private int scoreKeywords(String content) {
        long count = POWER_WORDS.stream().filter(content::contains).count();
        if (count >= 8) return 25;
        if (count >= 5) return 20;
        if (count >= 3) return 15;
        if (count >= 1) return 10;
        return 5;
    }

    private int scoreStructure(String content) {
        long sectionsFound = SECTION_KEYWORDS.stream().filter(content::contains).count();
        if (sectionsFound >= 5) return 25;
        if (sectionsFound >= 4) return 20;
        if (sectionsFound >= 3) return 15;
        if (sectionsFound >= 2) return 10;
        return 5;
    }

    private int scoreLength(String content) {
        int wordCount = content.split("\\s+").length;
        if (wordCount >= 300 && wordCount <= 700) return 20;
        if (wordCount >= 200 && wordCount < 300) return 15;
        if (wordCount >= 700 && wordCount <= 900) return 15;
        if (wordCount >= 100) return 10;
        return 5;
    }

    private int scoreTechSkills(String content) {
        long count = TECH_KEYWORDS.stream().filter(content::contains).count();
        if (count >= 8) return 20;
        if (count >= 5) return 16;
        if (count >= 3) return 12;
        if (count >= 1) return 8;
        return 4;
    }

    private int scoreContactInfo(String content) {
        int score = 0;
        if (content.contains("@")) score += 4;
        if (content.matches(".*\\d{10}.*") || content.contains("phone") || content.contains("mobile")) score += 3;
        if (content.contains("linkedin") || content.contains("github") || content.contains("portfolio")) score += 3;
        return Math.min(score, 10);
    }

    private String generateFeedback(int total, Map<String, Integer> breakdown) {
        StringBuilder sb = new StringBuilder();

        if (total >= 85) {
            sb.append("Excellent resume! Your resume is well-structured and ATS-friendly. ");
        } else if (total >= 70) {
            sb.append("Good resume with room for improvement. ");
        } else if (total >= 50) {
            sb.append("Average resume. Consider making significant improvements. ");
        } else {
            sb.append("Your resume needs substantial work. ");
        }

        breakdown.forEach((key, value) -> {
            int max = getMaxScore(key);
            if (value < max * 0.6) {
                sb.append("Improve your ").append(key.toLowerCase()).append(". ");
            }
        });

        return sb.toString().trim();
    }

    private int getMaxScore(String category) {
        return switch (category) {
            case "Keywords & Action Verbs" -> 25;
            case "Structure & Sections" -> 25;
            case "Content Length" -> 20;
            case "Technical Skills" -> 20;
            case "Contact Information" -> 10;
            default -> 10;
        };
    }
}
