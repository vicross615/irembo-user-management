package com.irembo.useraccountmanagement.service;

import com.irembo.useraccountmanagement.models.Session;
import com.irembo.useraccountmanagement.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

/**
 * Created by USER on 5/6/2023.
 */
@Service
public class SessionServiceImpl implements SessionService {

    @Autowired
    private SessionRepository sessionRepository;

    @Override
    public String createSession(String username, String accessToken) {
        String sessionId = UUID.randomUUID().toString();
        Session session = new Session(sessionId, username, accessToken);
        sessionRepository.save(session);
        return sessionId;
    }

    @Override
    public String getAccessToken(String sessionId) {
        Session session = sessionRepository.findById(sessionId).orElse(null);
        return session != null ? session.getAccessToken() : null;
    }

    @Override
    public void deleteSession(String sessionId) {
        sessionRepository.deleteById(sessionId);
    }

    @Override
    public void storeMfaCode(String sessionId, String mfaCode) {
        Optional<Session> sessionOptional = sessionRepository.findById(sessionId);
        if (sessionOptional.isPresent()) {
            Session session = sessionOptional.get();
            session.setMfaCode(mfaCode);
            sessionRepository.save(session);
        }
    }



//    @Override
//    public String getMfaCode(String sessionId) {
//        Optional<Session> sessionOptional = sessionRepository.findById(sessionId);
//        return sessionOptional.map(Session::getMfaCode).orElse(null);
//    }
}