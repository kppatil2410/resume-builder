package com.resumebuilder.dto;

import jakarta.validation.constraints.NotBlank;

public class AtsCheckRequest {

    @NotBlank(message = "Resume content is required")
    private String resumeContent;

    @NotBlank(message = "Job description is required")
    private String jobDescription;

    public String getResumeContent() { return resumeContent; }
    public void setResumeContent(String resumeContent) { this.resumeContent = resumeContent; }
    public String getJobDescription() { return jobDescription; }
    public void setJobDescription(String jobDescription) { this.jobDescription = jobDescription; }
}
