package com.example.hello;

import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "events")
public class ActivityEvent {

    @Id
    private String id;

    private String message;
    private Instant createdAt;

    // MongoDB requires a no-arg constructor
    public ActivityEvent() {
    }

    public ActivityEvent(String message) {
        this.message = message;
        this.createdAt = Instant.now();
    }

    public String getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
