package com.resumebuilder.service;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.*;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayOutputStream;

@Service
public class PdfService {

    public byte[] generatePdf(String resumeContent, String title) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4, 50, 50, 50, 50);
            PdfWriter.getInstance(document, baos);
            document.open();

            // Fonts
            Font titleFont = new Font(Font.HELVETICA, 20, Font.BOLD, new Color(30, 30, 30));
            Font sectionFont = new Font(Font.HELVETICA, 12, Font.BOLD, new Color(50, 100, 200));
            Font bodyFont = new Font(Font.HELVETICA, 10, Font.NORMAL, new Color(60, 60, 60));
            Font lineFont = new Font(Font.HELVETICA, 8, Font.NORMAL, new Color(150, 150, 150));

            // Title
            Paragraph titlePara = new Paragraph(title, titleFont);
            titlePara.setAlignment(Element.ALIGN_CENTER);
            titlePara.setSpacingAfter(5);
            document.add(titlePara);

            // Divider line
            Paragraph divider = new Paragraph("─".repeat(80), lineFont);
            divider.setAlignment(Element.ALIGN_CENTER);
            divider.setSpacingAfter(15);
            document.add(divider);

            // Parse and render content sections
            String[] lines = resumeContent.split("\n");
            for (String line : lines) {
                if (line.isBlank()) {
                    document.add(new Paragraph(" "));
                    continue;
                }

                // Detect section headers (all caps or ends with colon)
                if (isSectionHeader(line)) {
                    Paragraph section = new Paragraph(line.trim(), sectionFont);
                    section.setSpacingBefore(10);
                    section.setSpacingAfter(4);
                    document.add(section);

                    // Underline effect
                    Paragraph underline = new Paragraph("─".repeat(40), lineFont);
                    underline.setSpacingAfter(6);
                    document.add(underline);
                } else if (line.trim().startsWith("•") || line.trim().startsWith("-")) {
                    // Bullet points
                    Paragraph bullet = new Paragraph("  " + line.trim(), bodyFont);
                    bullet.setSpacingAfter(3);
                    document.add(bullet);
                } else {
                    Paragraph para = new Paragraph(line.trim(), bodyFont);
                    para.setSpacingAfter(3);
                    document.add(para);
                }
            }

            document.close();
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate PDF: " + e.getMessage(), e);
        }
    }

    private boolean isSectionHeader(String line) {
        String trimmed = line.trim();
        if (trimmed.isEmpty()) return false;
        // All caps with at least 3 chars, or ends with colon
        return (trimmed.equals(trimmed.toUpperCase()) && trimmed.length() >= 3 && trimmed.matches("[A-Z\\s&/]+"))
                || (trimmed.endsWith(":") && trimmed.length() > 3 && !trimmed.contains(" "));
    }
}
