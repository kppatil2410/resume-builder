package com.resumebuilder.dto;

import java.time.LocalDateTime;

public class ResumeResponse {
    private Long id;
    private String title;
    private String content;
    private String improvedContent;
    private String templateName;
    private Integer score;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ResumeResponse() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getImprovedContent() { return improvedContent; }
    public void setImprovedContent(String improvedContent) { this.improvedContent = improvedContent; }
    public String getTemplateName() { return templateName; }
    public void setTemplateName(String templateName) { this.templateName = templateName; }
    public Integer getScore() { return score; }
    public void setScore(Integer score) { this.score = score; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
