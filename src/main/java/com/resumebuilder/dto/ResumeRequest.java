package com.resumebuilder.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

public class ResumeRequest {

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Full name is required")
    private String fullName;

    @NotBlank(message = "Email is required")
    private String email;

    private String phone;
    private String location;
    private String summary;
    private List<ExperienceDto> experience;
    private List<EducationDto> education;
    private List<String> skills;
    private List<ProjectDto> projects;
    private String templateName;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }
    public List<ExperienceDto> getExperience() { return experience; }
    public void setExperience(List<ExperienceDto> experience) { this.experience = experience; }
    public List<EducationDto> getEducation() { return education; }
    public void setEducation(List<EducationDto> education) { this.education = education; }
    public List<String> getSkills() { return skills; }
    public void setSkills(List<String> skills) { this.skills = skills; }
    public List<ProjectDto> getProjects() { return projects; }
    public void setProjects(List<ProjectDto> projects) { this.projects = projects; }
    public String getTemplateName() { return templateName; }
    public void setTemplateName(String templateName) { this.templateName = templateName; }

    public static class ExperienceDto {
        private String company;
        private String position;
        private String startDate;
        private String endDate;
        private String description;

        public String getCompany() { return company; }
        public void setCompany(String company) { this.company = company; }
        public String getPosition() { return position; }
        public void setPosition(String position) { this.position = position; }
        public String getStartDate() { return startDate; }
        public void setStartDate(String startDate) { this.startDate = startDate; }
        public String getEndDate() { return endDate; }
        public void setEndDate(String endDate) { this.endDate = endDate; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }

    public static class EducationDto {
        private String institution;
        private String degree;
        private String field;
        private String graduationYear;
        private String gpa;

        public String getInstitution() { return institution; }
        public void setInstitution(String institution) { this.institution = institution; }
        public String getDegree() { return degree; }
        public void setDegree(String degree) { this.degree = degree; }
        public String getField() { return field; }
        public void setField(String field) { this.field = field; }
        public String getGraduationYear() { return graduationYear; }
        public void setGraduationYear(String graduationYear) { this.graduationYear = graduationYear; }
        public String getGpa() { return gpa; }
        public void setGpa(String gpa) { this.gpa = gpa; }
    }

    public static class ProjectDto {
        private String name;
        private String description;
        private String technologies;
        private String link;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getTechnologies() { return technologies; }
        public void setTechnologies(String technologies) { this.technologies = technologies; }
        public String getLink() { return link; }
        public void setLink(String link) { this.link = link; }
    }
}
