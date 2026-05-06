# AI Resume Builder & Analyzer

A full-stack web application to build, improve, score, and analyze resumes using AI.

## Tech Stack

- **Backend**: Java 17+, Spring Boot 3.2, Spring Security, Spring Data JPA
- **Frontend**: HTML5, CSS3, Vanilla JavaScript
- **Database**: MySQL 8+
- **AI**: OpenAI GPT-3.5-turbo (optional — works in demo mode without a key)
- **PDF**: OpenPDF (iText fork)
- **Auth**: JWT (JSON Web Tokens)

## Features

- User registration & login with JWT authentication
- Resume builder with form-based input (experience, education, skills, projects)
- 3 CSS-based resume templates (Modern, Classic, Minimal)
- AI-powered resume improvement via OpenAI API
- Resume scoring system (out of 100) with breakdown
- ATS keyword checker — compare resume vs job description
- PDF export/download
- Resume history with per-user storage
- Clean, responsive dashboard

## Project Structure

```
resume-builder/
├── src/main/java/com/resumebuilder/
│   ├── ResumeBuilderApplication.java
│   ├── config/          SecurityConfig.java
│   ├── controller/      AuthController.java, ResumeController.java
│   ├── dto/             Request/Response DTOs
│   ├── exception/       GlobalExceptionHandler.java
│   ├── model/           User.java, Resume.java
│   ├── repository/      UserRepository.java, ResumeRepository.java
│   ├── security/        JwtUtil.java, JwtAuthFilter.java, CustomUserDetailsService.java
│   └── service/         AuthService.java, ResumeService.java, OpenAiService.java,
│                        ResumeScoreService.java, AtsService.java, PdfService.java
└── src/main/resources/
    ├── application.properties
    ├── schema.sql
    └── static/
        ├── index.html, login.html, register.html
        ├── dashboard.html, resume-builder.html, history.html
        ├── css/style.css
        └── js/auth.js, builder.js, dashboard.js, history.js
```

## Setup & Run

### 1. Prerequisites

- Java 17 or higher
- Maven 3.6+
- MySQL 8.0+

### 2. Database Setup

```sql
CREATE DATABASE resume_builder;
```

Or run the provided `src/main/resources/schema.sql`.

### 3. Configure application.properties

Edit `src/main/resources/application.properties`:

```properties
# Update these values:
spring.datasource.url=jdbc:mysql://localhost:3306/resume_builder?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=YOUR_MYSQL_PASSWORD

# JWT secret (use a long random string, min 32 chars)
jwt.secret=your_very_long_secret_key_here_at_least_32_characters

# OpenAI (optional — app works in demo mode without it)
openai.api.key=sk-your-openai-api-key-here
```

### 4. Build & Run

```bash
# Build
mvn clean package -DskipTests

# Run
java -jar target/resume-builder-1.0.0.jar
```

Or run directly with Maven:

```bash
mvn spring-boot:run
```

### 5. Open in Browser

Navigate to: **http://localhost:8080**

## API Endpoints

| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| POST | `/api/auth/register` | Register new user | No |
| POST | `/api/auth/login` | Login | No |
| POST | `/api/resume/generate` | Generate resume | Yes |
| POST | `/api/resume/improve` | AI improve resume | Yes |
| POST | `/api/resume/score` | Score resume | Yes |
| POST | `/api/resume/ats-check` | ATS keyword check | Yes |
| GET | `/api/resume/history` | Get all resumes | Yes |
| GET | `/api/resume/{id}` | Get single resume | Yes |
| GET | `/api/resume/{id}/download` | Download PDF | Yes |
| POST | `/api/resume/download-pdf` | Download PDF from content | Yes |

## Notes

- Without an OpenAI API key, the AI Improve feature returns a demo response
- The ATS checker and scoring work fully without any API key
- PDF generation works offline using OpenPDF
- JWT tokens expire after 24 hours (configurable in `jwt.expiration`)
