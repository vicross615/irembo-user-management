package com.irembo.useraccountmanagement.service;

import com.irembo.useraccountmanagement.models.Session;

/**
 * Created by USER on 5/6/2023.
 */
public interface SessionService {
    String createSession(String username, String accessToken);
    String getAccessToken(String sessionId);
    void deleteSession(String sessionId);
    void storeMfaCode(String sessionId, String mfaCode);
    Session getMfaCode(String sessionId);
}