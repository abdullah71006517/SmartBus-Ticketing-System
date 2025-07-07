package com.example.demo1.Model;

import java.sql.Timestamp;

public class Feedback {
    private int id;
    private String username;
    private String cleanlinessRating;
    private String behaviorRating;
    private String serviceRating;
    private String message;
    private Timestamp submittedAt;

    public Feedback(String username, String cleanlinessRating, String behaviorRating, String serviceRating, String message) {
        this.username = username;
        this.cleanlinessRating = cleanlinessRating;
        this.behaviorRating = behaviorRating;
        this.serviceRating = serviceRating;
        this.message = message;
    }

    // Getters
    public String getUsername() { return username; }
    public String getCleanlinessRating() { return cleanlinessRating; }
    public String getBehaviorRating() { return behaviorRating; }
    public String getServiceRating() { return serviceRating; }
    public String getMessage() { return message; }

    // Optional: setters and ID/time access if needed
}
