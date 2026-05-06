package com.resumebuilder.dto;

public class AuthResponse {
    private String token;
    private String name;
    private String email;
    private Long userId;
    private String message;

    public AuthResponse() {}

    public AuthResponse(String token, String name, String email, Long userId, String message) {
        this.token = token;
        this.name = name;
        this.email = email;
        this.userId = userId;
        this.message = message;
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
