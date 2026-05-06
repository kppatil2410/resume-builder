package com.resumebuilder.controller;

import com.resumebuilder.dto.*;
import com.resumebuilder.service.ResumeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/resume")
public class ResumeController {

    @Autowired
    private ResumeService resumeService;

    @PostMapping("/generate")
    public ResponseEntity<ApiResponse<ResumeResponse>> generateResume(
            @Valid @RequestBody ResumeRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            ResumeResponse response = resumeService.generateResume(request, userDetails.getUsername());
            return ResponseEntity.ok(ApiResponse.success(response, "Resume generated successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/improve")
    public ResponseEntity<ApiResponse<ResumeResponse>> improveResume(
            @Valid @RequestBody ImproveRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            ResumeResponse response = resumeService.improveResume(request, userDetails.getUsername());
            return ResponseEntity.ok(ApiResponse.success(response, "Resume improved successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/score")
    public ResponseEntity<ApiResponse<ScoreResponse>> scoreResume(
            @RequestBody Map<String, String> body,
            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            String content = body.get("content");
            ScoreResponse response = resumeService.scoreResume(content, userDetails.getUsername());
            return ResponseEntity.ok(ApiResponse.success(response, "Score calculated"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/ats-check")
    public ResponseEntity<ApiResponse<AtsCheckResponse>> atsCheck(
            @Valid @RequestBody AtsCheckRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            AtsCheckResponse response = resumeService.atsCheck(request, userDetails.getUsername());
            return ResponseEntity.ok(ApiResponse.success(response, "ATS check completed"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/history")
    public ResponseEntity<ApiResponse<List<ResumeResponse>>> getHistory(
            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            List<ResumeResponse> history = resumeService.getHistory(userDetails.getUsername());
            return ResponseEntity.ok(ApiResponse.success(history, "History retrieved"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ResumeResponse>> getResume(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            ResumeResponse response = resumeService.getResumeById(id, userDetails.getUsername());
            return ResponseEntity.ok(ApiResponse.success(response, "Resume retrieved"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<byte[]> downloadPdf(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            byte[] pdf = resumeService.downloadPdf(id, userDetails.getUsername());
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"resume.pdf\"")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdf);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/download-pdf")
    public ResponseEntity<byte[]> downloadPdfFromContent(
            @RequestBody Map<String, String> body,
            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            String content = body.get("content");
            String title = body.getOrDefault("title", "My Resume");
            byte[] pdf = resumeService.downloadPdfFromContent(content, title);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"resume.pdf\"")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdf);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
