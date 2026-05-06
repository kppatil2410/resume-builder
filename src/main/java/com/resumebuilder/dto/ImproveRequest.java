package com.resumebuilder.dto;

import jakarta.validation.constraints.NotBlank;

public class ImproveRequest {

    @NotBlank(message = "Resume content is required")
    private String content;

    private Long resumeId;

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public Long getResumeId() { return resumeId; }
    public void setResumeId(Long resumeId) { this.resumeId = resumeId; }
}
