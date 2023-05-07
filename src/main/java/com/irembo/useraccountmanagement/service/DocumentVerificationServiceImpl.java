package com.irembo.useraccountmanagement.service;

import com.irembo.useraccountmanagement.models.DocumentVerification;
import com.irembo.useraccountmanagement.models.User;
import com.irembo.useraccountmanagement.models.VerificationStatus;
import com.irembo.useraccountmanagement.repository.DocumentVerificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by USER on 5/5/2023.
 */
@Service
public class DocumentVerificationServiceImpl implements DocumentVerificationService {

    @Autowired
    private DocumentVerificationRepository documentVerificationRepository;

    @Autowired
    private UserService userService;

    @Override
    public DocumentVerification submitVerification(DocumentVerification documentVerification) {
        User user = documentVerification.getUser();
        user.setVerificationStatus(VerificationStatus.PENDING_VERIFICATION);
        userService.updateUser(user);
        return documentVerificationRepository.save(documentVerification);
    }

    @Override
    public DocumentVerification findByUserId(Long userId) {
        return documentVerificationRepository.findByUserId(userId).orElse(null);
    }

    @Override
    public DocumentVerification updateVerificationStatus(Long userId, VerificationStatus status) {
        DocumentVerification documentVerification = findByUserId(userId);
        if (documentVerification != null) {
            User user = documentVerification.getUser();
            user.setVerificationStatus(status);
            userService.updateUser(user);
        }
        return documentVerification;
    }
}