package com.irembo.useraccountmanagement.service;

import com.irembo.useraccountmanagement.models.Session;
import com.irembo.useraccountmanagement.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by USER on 5/5/2023.
 */
@Service
public class MfaServiceImpl implements MfaService {

    private static final int MFA_CODE_LENGTH = 6;
    private static final int MFA_CODE_EXPIRATION_MINUTES = 5;
    private static final int MFA_CODE_VALIDITY_MINUTES = 10;

    @Autowired
    private SessionService sessionService;
    private final SecureRandom secureRandom = new SecureRandom();

    @Override
    public String generateAndSendMfaCode(String  sessionId) {
        String code = generateMfaCode();
        System.out.println(code);
        sessionService.storeMfaCode(sessionId, code);
        // Send the code to the user, e.g., via SMS or email
        return code;
    }


    @Override
    public boolean verifyMfaCode(String  session, String code) {
        Session storedCode = sessionService.getSessionById(session);
        if (storedCode != null && storedCode.getMfaCode().equals(code)) {
            long currentTimeMillis = System.currentTimeMillis();
            long codeAgeMillis = currentTimeMillis - storedCode.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
            if (TimeUnit.MILLISECONDS.toMinutes(codeAgeMillis) <= MFA_CODE_EXPIRATION_MINUTES) {
                sessionService.deleteSession(session);
                return true;
            }
        }
        return false;
    }

//    public boolean verifyMfaCodes(User user, String code) {
//        Session session = sessionService.getSessionByUser(user.getUsername());
//        if (session != null) {
//            LocalDateTime updatedAt = session.getUpdatedAt();
//            long minutesSinceUpdate = updatedAt.until(LocalDateTime.now(), ChronoUnit.MINUTES);
//            if (minutesSinceUpdate < MFA_CODE_VALIDITY_MINUTES && code.equals(session.getMfaCode())) {
//                return true;
//            }
//        }
//        return false;
//    }

    private String generateMfaCode() {
        StringBuilder codeBuilder = new StringBuilder(MFA_CODE_LENGTH);
        for (int i = 0; i < MFA_CODE_LENGTH; i++) {
            codeBuilder.append(secureRandom.nextInt(10));
        }
        return codeBuilder.toString();
    }

    private static class MfaCode {
        private final String code;
        private final long creationTime;

        public MfaCode(String code, long creationTime) {
            this.code = code;
            this.creationTime = creationTime;
        }

        public String getCode() {
            return code;
        }

        public long getCreationTime() {
            return creationTime;
        }
    }
}