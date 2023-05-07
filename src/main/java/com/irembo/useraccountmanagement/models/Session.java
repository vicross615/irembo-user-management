package com.irembo.useraccountmanagement.models;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Created by USER on 5/6/2023.
 */
@Entity
@Table(name = "sessions")
public class Session {

    @Id
    private String sessionId;
    private String accessToken;

    private boolean mfaPending;
    private String mfaCode;
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;


    public Session() {
    }

    public Session(String sessionId, String accessToken) {
        this.sessionId = sessionId;
        this.accessToken = accessToken;
    }

    public Session(String sessionId, String username, String accessToken) {
        this.sessionId = sessionId;
        this.accessToken = accessToken;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public boolean isMfaPending() {
        return mfaPending;
    }

    public void setMfaPending(boolean mfaPending) {
        this.mfaPending = mfaPending;
    }
    public String getMfaCode() {
        return mfaCode;
    }

    public void setMfaCode(String mfaCode) {
        this.mfaCode = mfaCode;
    }

    // ... existing constructors, getters, and setters

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}