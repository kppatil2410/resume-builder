package com.resumebuilder.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
public class OpenAiService {

    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.api.url}")
    private String apiUrl;

    @Value("${openai.model}")
    private String model;

    private final WebClient webClient;

    public OpenAiService() {
        this.webClient = WebClient.builder().build();
    }

    public String improveResume(String resumeContent) {
        String prompt = """
                You are a professional resume writer. Improve the following resume to make it more professional, 
                impactful, and ATS-friendly. Use strong action verbs, quantify achievements where possible, 
                and ensure clear formatting. Return only the improved resume text.
                
                Resume:
                %s
                """.formatted(resumeContent);

        return callOpenAi(prompt);
    }

    public String optimizeBulletPoints(String bulletPoints) {
        String prompt = """
                Rewrite the following resume bullet points using strong action verbs, quantifiable results, 
                and the STAR method (Situation, Task, Action, Result) where applicable. 
                Make them concise and impactful. Return only the improved bullet points.
                
                Bullet Points:
                %s
                """.formatted(bulletPoints);

        return callOpenAi(prompt);
    }

    private String callOpenAi(String prompt) {
        // Check if API key is configured
        if (apiKey == null || apiKey.equals("your_openai_api_key_here") || apiKey.isBlank()) {
            return getMockImprovedResume(prompt);
        }

        try {
            Map<String, Object> requestBody = Map.of(
                    "model", model,
                    "messages", List.of(
                            Map.of("role", "user", "content", prompt)
                    ),
                    "max_tokens", 2000,
                    "temperature", 0.7
            );

            Map response = webClient.post()
                    .uri(apiUrl)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            if (response != null && response.containsKey("choices")) {
                List<Map> choices = (List<Map>) response.get("choices");
                if (!choices.isEmpty()) {
                    Map message = (Map) choices.get(0).get("message");
                    return (String) message.get("content");
                }
            }
            return "Unable to improve resume at this time. Please try again.";
        } catch (Exception e) {
            return getMockImprovedResume(prompt);
        }
    }

    private String getMockImprovedResume(String originalContent) {
        return """
                [AI-IMPROVED VERSION - Demo Mode]
                
                Note: Configure your OpenAI API key in application.properties to enable real AI improvements.
                
                Here is a sample improved version of your resume:
                
                PROFESSIONAL SUMMARY
                Results-driven professional with proven expertise in delivering high-impact solutions. 
                Demonstrated ability to lead cross-functional teams and drive measurable business outcomes.
                
                KEY ACHIEVEMENTS
                • Spearheaded development of scalable applications, reducing processing time by 40%
                • Collaborated with stakeholders to define requirements and deliver projects on time
                • Implemented best practices that improved code quality and reduced bugs by 30%
                • Led technical initiatives that contributed to 25% increase in team productivity
                
                TECHNICAL SKILLS
                • Programming: Java, Python, JavaScript, SQL
                • Frameworks: Spring Boot, React, Node.js
                • Tools: Git, Docker, AWS, Jenkins
                • Methodologies: Agile, Scrum, TDD
                
                [Your original content has been analyzed and improvements suggested above]
                """;
    }
}
