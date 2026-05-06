package com.resumebuilder.service;

import com.resumebuilder.dto.*;
import com.resumebuilder.model.Resume;
import com.resumebuilder.model.User;
import com.resumebuilder.repository.ResumeRepository;
import com.resumebuilder.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ResumeService {

    @Autowired
    private ResumeRepository resumeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OpenAiService openAiService;

    @Autowired
    private ResumeScoreService scoreService;

    @Autowired
    private AtsService atsService;

    @Autowired
    private PdfService pdfService;

    public ResumeResponse generateResume(ResumeRequest request, String userEmail) {
        User user = getUser(userEmail);

        String content = buildResumeContent(request);
        ScoreResponse score = scoreService.calculateScore(content);

        Resume resume = new Resume();
        resume.setUser(user);
        resume.setTitle(request.getTitle());
        resume.setContent(content);
        resume.setTemplateName(request.getTemplateName() != null ? request.getTemplateName() : "modern");
        resume.setScore(score.getTotalScore());

        resumeRepository.save(resume);
        return mapToResponse(resume);
    }

    public ResumeResponse improveResume(ImproveRequest request, String userEmail) {
        User user = getUser(userEmail);
        String improved = openAiService.improveResume(request.getContent());

        if (request.getResumeId() != null) {
            resumeRepository.findByIdAndUser(request.getResumeId(), user).ifPresent(resume -> {
                resume.setImprovedContent(improved);
                resumeRepository.save(resume);
            });
        }

        ResumeResponse response = new ResumeResponse();
        response.setContent(request.getContent());
        response.setImprovedContent(improved);
        return response;
    }

    public ScoreResponse scoreResume(String content, String userEmail) {
        return scoreService.calculateScore(content);
    }

    public AtsCheckResponse atsCheck(AtsCheckRequest request, String userEmail) {
        return atsService.checkAts(request.getResumeContent(), request.getJobDescription());
    }

    public List<ResumeResponse> getHistory(String userEmail) {
        User user = getUser(userEmail);
        return resumeRepository.findByUserOrderByCreatedAtDesc(user)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public ResumeResponse getResumeById(Long id, String userEmail) {
        User user = getUser(userEmail);
        Resume resume = resumeRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new RuntimeException("Resume not found"));
        return mapToResponse(resume);
    }

    public byte[] downloadPdf(Long id, String userEmail) {
        User user = getUser(userEmail);
        Resume resume = resumeRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new RuntimeException("Resume not found"));

        String content = resume.getImprovedContent() != null
                ? resume.getImprovedContent()
                : resume.getContent();

        return pdfService.generatePdf(content, resume.getTitle());
    }

    public byte[] downloadPdfFromContent(String content, String title) {
        return pdfService.generatePdf(content, title != null ? title : "My Resume");
    }

    private String buildResumeContent(ResumeRequest req) {
        StringBuilder sb = new StringBuilder();

        // Header
        sb.append(req.getFullName().toUpperCase()).append("\n");
        sb.append(req.getEmail());
        if (req.getPhone() != null && !req.getPhone().isBlank()) {
            sb.append(" | ").append(req.getPhone());
        }
        if (req.getLocation() != null && !req.getLocation().isBlank()) {
            sb.append(" | ").append(req.getLocation());
        }
        sb.append("\n\n");

        // Summary
        if (req.getSummary() != null && !req.getSummary().isBlank()) {
            sb.append("PROFESSIONAL SUMMARY\n");
            sb.append(req.getSummary()).append("\n\n");
        }

        // Experience
        if (req.getExperience() != null && !req.getExperience().isEmpty()) {
            sb.append("WORK EXPERIENCE\n");
            for (ResumeRequest.ExperienceDto exp : req.getExperience()) {
                sb.append(exp.getPosition()).append(" - ").append(exp.getCompany()).append("\n");
                sb.append(exp.getStartDate()).append(" - ").append(
                        exp.getEndDate() != null ? exp.getEndDate() : "Present").append("\n");
                if (exp.getDescription() != null && !exp.getDescription().isBlank()) {
                    for (String line : exp.getDescription().split("\n")) {
                        sb.append("• ").append(line.trim()).append("\n");
                    }
                }
                sb.append("\n");
            }
        }

        // Education
        if (req.getEducation() != null && !req.getEducation().isEmpty()) {
            sb.append("EDUCATION\n");
            for (ResumeRequest.EducationDto edu : req.getEducation()) {
                sb.append(edu.getDegree()).append(" in ").append(edu.getField()).append("\n");
                sb.append(edu.getInstitution());
                if (edu.getGraduationYear() != null) {
                    sb.append(" | ").append(edu.getGraduationYear());
                }
                if (edu.getGpa() != null && !edu.getGpa().isBlank()) {
                    sb.append(" | GPA: ").append(edu.getGpa());
                }
                sb.append("\n\n");
            }
        }

        // Skills
        if (req.getSkills() != null && !req.getSkills().isEmpty()) {
            sb.append("SKILLS\n");
            sb.append(String.join(" • ", req.getSkills())).append("\n\n");
        }

        // Projects
        if (req.getProjects() != null && !req.getProjects().isEmpty()) {
            sb.append("PROJECTS\n");
            for (ResumeRequest.ProjectDto proj : req.getProjects()) {
                sb.append(proj.getName()).append("\n");
                if (proj.getTechnologies() != null && !proj.getTechnologies().isBlank()) {
                    sb.append("Technologies: ").append(proj.getTechnologies()).append("\n");
                }
                if (proj.getDescription() != null && !proj.getDescription().isBlank()) {
                    sb.append("• ").append(proj.getDescription()).append("\n");
                }
                if (proj.getLink() != null && !proj.getLink().isBlank()) {
                    sb.append("Link: ").append(proj.getLink()).append("\n");
                }
                sb.append("\n");
            }
        }

        return sb.toString().trim();
    }

    private ResumeResponse mapToResponse(Resume resume) {
        ResumeResponse response = new ResumeResponse();
        response.setId(resume.getId());
        response.setTitle(resume.getTitle());
        response.setContent(resume.getContent());
        response.setImprovedContent(resume.getImprovedContent());
        response.setTemplateName(resume.getTemplateName());
        response.setScore(resume.getScore());
        response.setCreatedAt(resume.getCreatedAt());
        response.setUpdatedAt(resume.getUpdatedAt());
        return response;
    }

    private User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
    }
}
